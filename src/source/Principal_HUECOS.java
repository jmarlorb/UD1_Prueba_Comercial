package source;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Principal_HUECOS {
	private static File home = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "AD"
			+ System.getProperty("file.separator") + "UD1_PruebaComercial"); // user.home\AD\UD1_PruebaComercial
	private static Properties configuracion = init();
	private static Set<Comercial> comerciales = new HashSet<Comercial>();
	private static Set<Empresa> empresas = new HashSet<Empresa>();
	private static Set<Visita> visitas = new HashSet<Visita>();

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.out.println("-- -- INICIO PRINCIPAL -- --");

		// Datos por defecto
		Comercial c1 = new Comercial("Ana Anaya");
		Comercial c2 = new Comercial("Bob Borger");
		comerciales.add(c1);
		comerciales.add(c2);

		Empresa e1 = new Empresa(8587, "InfoTecno");
		Empresa e2 = new Empresa(4245, "TecnoPro");
		empresas.add(e1);
		empresas.add(e2);

		Visita v1 = new Visita(c1, new Date(2020 - 1900, 1, 7, 10, 30), e1);
		Visita v2 = new Visita(c1, new Date(2020 - 1900, 1, 1, 11, 00), e2);
		Visita v3 = new Visita(c2, new Date(), e2); // Ahora
		visitas.add(v1);
		visitas.add(v2);
		visitas.add(v3);

		System.out.println("Se han cargado los datos por defecto");

		System.out.println("Intentado cargar los comerciales desde fichero");
		cargarComerciales_Binario();
		System.out.println("Ahora hay: " + comerciales.size() + " comerciales");

		System.out.println("Intentado añadir un nuevo comercial");
		System.out.print("Introduce el nombre el nuevo comercial | Pulsa Intro para saltar este paso: ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		if (name.length() != 0) {
			annadeComerciales_Binario(new Comercial(name));
			System.out.println("Ahora hay: " + comerciales.size() + " comerciales");
		} else {
			System.out.println("No se ha añadido un nuevo comercial");
		}

		System.out.println("Intentado cargar las empresas desde fichero usando SAX");
		cargarEmpresas_XML_SAX();
		System.out.println("Ahora hay: " + empresas.size() + " empresas");
		
		System.out.println("Intentado generar el fichero de XML con las visitas usando DOM");
		guardarVisitasDOM();

		System.out.println("Intentado generar el fichero de texto con las visitas");
		guardarVisitas_TXT();

		System.out.println("-- -- FIN PRINCIPAL -- --");
		sc.close();

	}

	/*
	 * 0. (0.5 pts) Las clases Comercial, Visita y Empresa están incompletas. Añade
	 * lo necesario para que puedan ser almacenadas en archivos binarios. Comenta en
	 * el código las modificaciones necesarias
	 * 
	 * He hecho que las tres implementen la interfaz serializable
	 * 
	 * 
	 * 
	 */
	public static Properties init() {
		System.out.println("-- INIT() --");
		File pPath = new File(home, "config.ini");
		Properties p = new Properties();
		if (pPath.exists() && pPath.canRead()) {
			FileReader fr = null;

			try {
				fr = new FileReader(pPath);
				p.load(fr);
				System.out.println("Propediades leidas.");
				fr.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		} else {
			FileWriter fw = null;
			try {
				pPath.createNewFile();
				fw = new FileWriter(pPath);
				p.put("file_xml_Empresas",
						home.getAbsolutePath() + System.getProperty("file.separator") + "empresas.xml");
				p.put("file_dat_Comerciales",
						home.getAbsolutePath() + System.getProperty("file.separator") + "comerciales.dat");
				p.put("file_txt_Visitas",
						home.getAbsolutePath() + System.getProperty("file.separator") + "visitas.txt");
				p.put("file_xml_Visitas",
						home.getAbsolutePath() + System.getProperty("file.separator") + "visitas.xml");
				p.store(fw, "Propiedades de Visitas");
				System.out.println("Propiedades escritas.");
				fw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return null;
			}
		}
		return p;
	}

	private static void cargarComerciales_Binario() {
		System.out.println("-- cargarComerciales_Binario() --");
		File cBin = new File(configuracion.getProperty("file_dat_Comerciales"));
		if (!cBin.exists()) {
			FileOutputStream FOS_cBin = null;
			try {
				cBin.createNewFile(); //Creamos el archivo.
				FOS_cBin = new FileOutputStream(cBin);
				ObjectOutputStream OOS_cBin = new ObjectOutputStream(FOS_cBin);
				for (Comercial c : comerciales) {
					OOS_cBin.writeObject(c); //Escribimos todos los comerciales en el.
				}
				FOS_cBin.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				try {
					FOS_cBin.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else {
			FileInputStream FIS_cBin = null;
			try {
				FIS_cBin = new FileInputStream(cBin);
				ObjectInputStream OIS = new ObjectInputStream(FIS_cBin);
				comerciales.clear();//Quitamos los datos de comerciales para evitar duplicas.
				while (true) {
					comerciales.add((Comercial) OIS.readObject()); //Leemos objetos y los añadimos al set comerciales.
				}
			} catch (EOFException e) {
				//Esta excepción representa el fin exitoso de la lectura, habiendo llegado al fin del fichero. 
				System.out.println("Lectura Completada Exitosamente");
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			} finally {
				try {
					FIS_cBin.close(); //Aseguramos el cierre de flujos.
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}

		}
	}

	private static void annadeComerciales_Binario(Comercial c) {
		System.out.println("-- annadeComerciales_Binario(" + c + ") --");
		if (comerciales.contains(c))
			System.out.println("El comercial " + c.getNombre() + " ya esta en la lista.");
		else {
			File cBin = new File(configuracion.getProperty("file_dat_Comerciales"));
			if (cBin.exists() && cBin.canWrite()) {
				FileOutputStream FOS_cBin = null;
				try {
					FOS_cBin = new FileOutputStream(cBin, true);
					ObjectOutputStream OOS_cBin = new ObjectOutputStream(FOS_cBin) {
						@Override
						protected void writeStreamHeader() throws IOException {
							// Dejamos el metodo de escribir cabecera vacío para que no haga nada y no estropea el archivo al introducir nuevos datos.
						};
					};
					OOS_cBin.writeObject(c);
					FOS_cBin.close();
					comerciales.add(c);
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				} finally {
					try {
						FOS_cBin.close(); //Aseguramos el cierre de flujos.
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
		}

	}

	private static void cargarEmpresas_XML_SAX() {
		System.out.println("-- cargarEmpresas_XML_SAX() --");
		//Inicialización del SAX
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser parser = factory.newSAXParser();

			XMLReader reader = parser.getXMLReader();

			reader.setContentHandler(new DefaultHandler() { //Creación del DefaultHandler
				private Empresa aux;
				private boolean empresa = false;
				private boolean cif = false;
				private boolean nombre = false;

				@Override
				public void startDocument() {
				}

				@Override
				public void endDocument() {
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes) {

					switch (qName) {
					case "empresa":
						aux = new Empresa(); // Creamos nuevo objeto Empresa cada vez que encontramos la apertura de la etiqueta correspondiente. 
						empresa = true;
						break;
					case "cif":
						cif = true;
						break;
					case "nombre":
						nombre = true;
						break;
					}
				}

				@Override
				public void endElement(String uri, String localName, String qName) {

					switch (qName) {
					case "empresa":
						empresas.add(aux); //Y la añadimos al set con el cierre de la etiqueta.
						empresa = false;
						break;
					case "cif":
						cif = false;
						break;
					case "nombre":
						nombre = false;
						break;
					}
				}

				@Override
				public void characters(char[] ch, int start, int length) {
					if (empresa) {
						String texto = new String(ch, start, length);
						texto = texto.replaceAll("[\t\n]", "");
						if (cif) // Rellenamos los atributos con el contenido de los nodos correspondientes.
							aux.setCif(Integer.parseInt(texto));
						if (nombre)
							aux.setNombre(texto);
					}
				}
			});
			reader.parse(configuracion.getProperty("file_xml_Empresas"));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private static void guardarVisitasDOM() {
		System.out.println("-- guardarVisitasDOM() --");
		//Inicialización del dom
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();

			DOMImplementation implementation = builder.getDOMImplementation();
			//Fin inicialización del dom

			Document document = implementation.createDocument(null, "Visitas", null); //Creación nodo raiz

			document.setXmlVersion("1.0");
			document.setXmlStandalone(true);
			Element raiz = document.getDocumentElement();
			for (Visita v : visitas) { //Añade elemento visita con nodos hijos de modo que represente el objeto Visita
				Element visita = anadeElemento(document, raiz, "Visita", "");
				anadeElemento(document, visita, "Comercial", v.getComercial().getNombre());
				anadeElemento(document, visita, "fecha",
						(v.getFecha().getYear() + 1900) + "/" + v.getFecha().getMonth() + "/" + v.getFecha().getDay()
								+ " " + v.getFecha().getHours() + ":" + v.getFecha().getMinutes());
				anadeElemento(document, visita, "Empresa", v.getEmpresa().getNombre());
			}
			document.normalize();
			File f = new File(configuracion.getProperty("file_xml_Visitas"));
			DOMSource source = new DOMSource(document); // Origen DOM
			StreamResult result = new StreamResult(f); // Destino Archivo
			try {
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Da el formato apropiado con tabulaciones al XML.
				transformer.transform(source, result); // Flujo de datos
			} catch (TransformerConfigurationException tce) {
				tce.printStackTrace();
			} catch (TransformerException te) {
				te.printStackTrace();
			} catch (TransformerFactoryConfigurationError tfce) {
				tfce.printStackTrace();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private static void guardarVisitas_TXT() {
		System.out.println("-- guardarVisitas_TXT() --");
		File f = new File(configuracion.getProperty("file_txt_Visitas"));
		FileWriter fw = null;
		try {
			fw = new FileWriter(f);
			for (Visita v : visitas) {
				fw.write((v.getFecha().getYear() + 1900) + "/" + v.getFecha().getMonth() + "/" + v.getFecha().getDay()
						+ " " + v.getFecha().getHours() + ":" + v.getFecha().getMinutes() + " - "
						+ v.getComercial().getNombre() + " -> " + v.getEmpresa().getCif() + ":"
						+ v.getEmpresa().getNombre()+"\n"); //Escribe linea de texto con el formato pedido
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close(); //Aseguramos el cierre de recursos
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// METODOS GENERICOS PARA DOM
	private static Element anadeElemento(Document doc, Element raiz, String clave, String valor) {
		// Creamos un elemento [clave]
		Element e = doc.createElement(clave); // <[clave]> ... </[clave]>

		// Añadimos el texto
		anadeNodoTextual(doc, e, valor); // <[clave]> [valor] </[clave]>

		// Pegamos el elemento a la raiz
		raiz.appendChild(e);
		return e;
	}

	private static Element anadeNodoTextual(Document doc, Element raiz, String valor) {
		// Pegamos el elemento a la raiz
		raiz.appendChild(doc.createTextNode(valor)); // <[raiz]> [valor] </[raiz]>
		return raiz;
	}

}
