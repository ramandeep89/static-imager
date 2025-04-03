package top.bagadbilla.model.generation.svg;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.bagadbilla.model.generation.BaseGeneration;

public abstract class BaseSVG extends BaseGeneration {

    protected final Document document;
    protected final int width, height;
    protected final Element svg;
    protected final Stack<Element> context = new Stack<>();

    protected BaseSVG(int width, int height) {
        super(width, height);
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        this.document = impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        svg = document.getDocumentElement();
        this.svg.setAttribute("width", String.valueOf(width));
        this.svg.setAttribute("height", String.valueOf(height));
        this.width = width;
        this.height = height;
    }

    protected final Element currentContext() {
        if (!context.isEmpty())
            return context.peek();
        else return svg;
    }

    protected final Element end() {
        return context.pop();
    }

    protected final Element currentNode() {
        Element context = this.currentContext();
        if (context.hasChildNodes())
            return (Element) context.getLastChild();
        else return context;
    }

    protected final void transform(@NotNull Map<String, List<String>> transformations) {
        this.currentNode().setAttribute("transform", transformations.entrySet().stream().map(
                        e -> e.getKey() + "(" + String.join(",", e.getValue()) + ")"
                ).collect(Collectors.joining(" "))
        );
    }

    protected final void setAttributes(Element el, @NotNull Map<String, String> attrs) {
        attrs.forEach(el::setAttribute);
    }

    protected final void rect(int x, int y, int width, int height, @NotNull Map<String, String> args) {
        Element rect = document.createElement("rect");
        this.currentContext().appendChild(rect);
        this.setAttributes(rect, Map.of(
                "x", String.valueOf(x),
                "y", String.valueOf(y),
                "width", String.valueOf(width),
                "height", String.valueOf(height)
        ));
        this.setAttributes(rect, args);
    }

    protected final void circle(int cx, int cy, int r, @NotNull Map<String, String> args) {
        Element circle = document.createElement("circle");
        this.currentContext().appendChild(circle);
        this.setAttributes(circle, Map.of(
                "cx", String.valueOf(cx),
                "cy", String.valueOf(cy),
                "r", String.valueOf(r)
        ));
        this.setAttributes(circle, args);
    }

    protected final void path(String str, @NotNull Map<String, String> args) {
        Element path = document.createElement("path");
        this.currentContext().appendChild(path);
        this.setAttributes(path, Map.of("d", str));
        this.setAttributes(path, args);
    }

    protected final void polyline(String str, @NotNull Map<String, String> args) {
        Element polyline = document.createElement("polyline");
        this.currentContext().appendChild(polyline);
        this.setAttributes(polyline, Map.of("points", str));
        this.setAttributes(polyline, args);
    }

    protected final void group(@NotNull Map<String, String> args) {
        Element group = document.createElement("g");
        this.currentContext().appendChild(group);
        this.context.push(group);
        this.setAttributes(group, args);
    }

    public abstract BaseSVG generateSVG();

    public String getSVG() {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(document), new StreamResult(sw));

            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
