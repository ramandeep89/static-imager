package top.bagadbilla.model.generation.svg;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import top.bagadbilla.model.generation.BaseGeneration;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class BaseSVG extends BaseGeneration {

    protected final Document document;
    protected final Element svg;
    protected final Stack<Element> context = new Stack<>();
    protected final int width, height;

    public BaseSVG(int width, int height) {
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
        if (!context.isEmpty()) return context.peek();
        else return svg;
    }

    protected final Element end() {
        return context.pop();
    }

    protected final void add(Element e) {
        context.push(e);
    }

    protected final Element currentNode() {
        Element context = this.currentContext();
        if (context.hasChildNodes()) return (Element) context.getLastChild();
        else return context;
    }

    protected final BaseSVG transform(@NotNull Map<String, List<Object>> transformations) {
        this.currentNode().setAttribute("transform", transformations.entrySet().stream().map(e -> e.getKey() + "(" + e.getValue().stream().map(Object::toString).collect(Collectors.joining(",")) + ")").collect(Collectors.joining(" ")));
        return this;
    }

    protected final BaseSVG setAttributes(Element el, @NotNull Map<String, Object> attrs) {
        attrs.forEach((k, v) -> el.setAttribute(k, String.valueOf(v)));
        return this;
    }

    protected final BaseSVG rect(double x, double y, String width, String height, @NotNull Map<String, Object> args) {
        Element rect = document.createElement("rect");
        this.currentContext().appendChild(rect);
        this.setAttributes(rect, Map.of("x", String.valueOf(x), "y", String.valueOf(y), "width", width, "height", height));
        this.setAttributes(rect, args);
        return this;
    }

    protected final BaseSVG rect(double x, double y, double width, double height, @NotNull Map<String, Object> args) {
        Element rect = document.createElement("rect");
        this.currentContext().appendChild(rect);
        this.setAttributes(rect, Map.of("x", String.valueOf(x), "y", String.valueOf(y), "width", String.valueOf(width), "height", String.valueOf(height)));
        this.setAttributes(rect, args);
        return this;
    }

    protected final BaseSVG rect(double[] params, @NotNull Map<String, Object> args) {
        if (params.length != 4) throw new IllegalArgumentException("strings.length != 4");
        else return rect(params[0], params[1], params[2], params[3], args);
    }

    protected final BaseSVG rect(List<double[]> lists, @NotNull Map<String, Object> args) {
        for (double[] params : lists) {
            rect(params, args);
        }
        return this;
    }

    protected final BaseSVG circle(double cx, double cy, double r, @NotNull Map<String, Object> args) {
        Element circle = document.createElement("circle");
        this.currentContext().appendChild(circle);
        this.setAttributes(circle, Map.of("cx", String.valueOf(cx), "cy", String.valueOf(cy), "r", String.valueOf(r)));
        this.setAttributes(circle, args);
        return this;
    }

    protected final BaseSVG path(String str, @NotNull Map<String, Object> args) {
        Element path = document.createElement("path");
        this.currentContext().appendChild(path);
        this.setAttributes(path, Map.of("d", str));
        this.setAttributes(path, args);
        return this;
    }

    protected final BaseSVG polyline(String str, @NotNull Map<String, Object> args) {
        Element polyline = document.createElement("polyline");
        this.currentContext().appendChild(polyline);
        this.setAttributes(polyline, Map.of("points", str));
        this.setAttributes(polyline, args);
        return this;
    }

    protected final BaseSVG polyline(List<String> strings, @NotNull Map<String, Object> args) {
        for (String str : strings) {
            polyline(str, args);
        }
        return this;
    }

    protected final BaseSVG group(@NotNull Map<String, Object> args) {
        Element group = document.createElement("g");
        this.currentContext().appendChild(group);
        this.context.push(group);
        this.setAttributes(group, args);
        return this;
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
