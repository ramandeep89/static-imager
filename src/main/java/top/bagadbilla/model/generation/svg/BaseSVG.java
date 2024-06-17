package top.bagadbilla.model.generation.svg;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import top.bagadbilla.model.generation.BaseGeneration;

public abstract class BaseSVG extends BaseGeneration {

    protected final Document document;
    protected final int width, height;
    private final StringWriter sw;

    public BaseSVG(StringWriter sw, int width, int height) {
        super(width, height);
        this.sw = sw;
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        this.document = impl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        this.width = width;
        this.height = height;
    }

    protected void writeSVG() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(document), new StreamResult(sw));
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
