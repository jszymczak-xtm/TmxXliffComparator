import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class XliffTmxComparator
{

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException
    {
        File tmxFile = new File("TMX.tmx");
        File xliffFile = new File("XLIFF.sdlxliff");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(tmxFile);
        Document doc2 = dBuilder.parse(xliffFile);
        doc.getDocumentElement().normalize();
        ArrayList<String> listXliff = createSourceList(doc2);
        ArrayList<String> listTmx = createTuvList(doc);
        createDifferencesList(listXliff, listTmx);

    }

    private static ArrayList<String> createSourceList(Document doc2)
    {
        NodeList segmentList = doc2.getElementsByTagName("source");
        ArrayList<String> listXliff = new ArrayList<String>();
        int numberOfSource = segmentList.getLength();
        for (int nodeId = 0; nodeId < numberOfSource; nodeId++)
        {
            Node oneSegment = segmentList.item(nodeId);
            Element oneSegmentElement = (Element) oneSegment;
            if (oneSegmentElement.hasChildNodes())
            {
                listXliff.add(oneSegmentElement.getTextContent());
            }
        }
        return listXliff;
    }

    private static ArrayList<String> createTuvList(Document doc)
    {
        NodeList tuvList = doc.getElementsByTagName("tuv");
        int numberOfTuv = tuvList.getLength();
        ArrayList<String> listTmx = new ArrayList<String>();
        for (int nodeId = 0; nodeId < numberOfTuv; nodeId++)
        {
            Node oneSegment = tuvList.item(nodeId);
            Element oneSegmentElement = (Element) oneSegment;
            if (oneSegmentElement.getAttribute("xml:lang").endsWith("JP"))
            {
                listTmx.add(oneSegmentElement.getTextContent());
            }
        }
        return listTmx;
    }

    private static void createDifferencesList(ArrayList<String> listXliff, ArrayList<String> listTmx) throws FileNotFoundException
    {
        ArrayList<String> listDifferences = new ArrayList<String>();
        for (String temp : listXliff)
        {
            if (listTmx.contains(temp))
            {

            } else
            {
                listDifferences.add(temp);
            }
        }
        File txtFile = new File("MissingSegments.txt");
        PrintStream stream = new PrintStream(txtFile);
        System.setOut(stream);
        System.out.println("Segments not found in TMX:");
        Iterator<String> itr = listDifferences.iterator();
        while (itr.hasNext())
        {
            System.out.println(itr.next());
        }
        stream.close();
        PrintStream consoleStream = new PrintStream(
                new FileOutputStream(FileDescriptor.out));
        System.setOut(consoleStream);
    }
}
