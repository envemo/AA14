import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

class Golosina {
	
	private String tipo;
	private String nombre;
	private int cantidadProducida;
	
	public Golosina(String tipo, String nombre, int cantidadProducida) {
		super();
		this.tipo = tipo;
		this.nombre = nombre;
		this.cantidadProducida = cantidadProducida;
	}
	
	@Override
	public String toString() {
		return tipo + " " + nombre + "		" + cantidadProducida;
	}
	
}

class Chocolate extends Golosina {

	public Chocolate(String nombre, int cantidadProducida) {
		super("Chocolate", nombre, cantidadProducida);
	}
	
}

interface comprobar {
	boolean produccionActiva(int temperatura);
}

public class AA14 implements comprobar {

	public static void main(String args[]) {
				
		try
		{
		
			//https://www.el-tiempo.net/api/json/v2/provincias/41
			String link = "https://www.el-tiempo.net/api/json/v2/provincias/41";

			URL url = new URL (link); //Declaro la URL
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Declaro la conexion
			conn.connect(); //Abro la conexion

			int tiempoRespuesta = conn.getResponseCode(); //Para manejo de 
														  //Situaciones
			
			if(tiempoRespuesta != 200)
			{
				throw new RuntimeException("HttpResponse" + tiempoRespuesta);
			}
			else
			{
				StringBuilder sb = new StringBuilder();
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					sb.append(sc.nextLine());
				}
				sc.close();
				JSONObject jsonObject = new JSONObject(sb.toString());
				System.out.println(jsonObject.getString("metadescripcion") + ":");
				System.out.println("----------------------------------------------------");
				int temperatura = 0;
				for (Object jsonObject2: jsonObject.getJSONArray("ciudades")) {
					if (((JSONObject) jsonObject2).getString("name").equals("Sevilla")) {
						JSONObject jsonObject3 = (JSONObject) ((JSONObject) jsonObject2).get("temperatures");
						temperatura = jsonObject3.getInt("max");
						System.out.println("- Temperatura de hoy: " + temperatura);
					}
				}
				try {
					if (new AA14().produccionActiva(temperatura)) {
						List<Chocolate> chocolates = new ArrayList<>();
						chocolates.add(new Chocolate("Blanco", 1000));
						chocolates.add(new Chocolate("Negro", 1500));
						chocolates.add(new Chocolate("con almendras", 1200));
						chocolates.add(new Chocolate("con castañas de caju", 1300));
						chocolates.add(new Chocolate("en rama", 100));
						chocolates.add(new Chocolate("con 70% de cacao", 1500));
						List<String> lineas = new ArrayList<>();
						for (Chocolate chocolate : chocolates) {
							System.out.println(chocolate);
							lineas.add(chocolate.toString());
						}
						Path file = Paths.get("salida_" + LocalDate.now() + ".txt");
						Files.write(file, lineas, StandardCharsets.UTF_8);
						file = Paths.get("Jenkinsfile");
						List<String> lineas2 = lineas;
						lineas = new ArrayList<String>();
						lineas.add("def date = new Date()");
						lineas.add("");
						lineas.add("pipeline {");
						lineas.add("	agent any");
						lineas.add("	stages {");
						lineas.add("		stage ('HolaMundo') {");
						lineas.add("			steps {");
						lineas.add("				echo \"Hoy es día \" + date.format('dd-MM-yyyy')");
						for (String linea : lineas2) {
							lineas.add("				echo \"" + linea + "\"");
						}
						lineas.add("			}");
						lineas.add("		}");
						lineas.add("	}");
						lineas.add("}");
						Files.write(file, lineas, StandardCharsets.UTF_8);
					}
				} catch (Exception e) {
					// TODO Bloque catch generado automáticamente
					e.printStackTrace();
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean produccionActiva(int temperatura) {
		if (temperatura <= 40) {
			return true;
		} else {
			System.out.println("Se ha superado el nivel de temperatura máxima permitida.");
			return false;
		}
	}
	
}

/**
 * (!) AA14 (!)
ESTRUCTURA DEL PROGRAMA:
- Proyecto Maven (Lo van a necesitar para el JSON)
- Herencia entre clases
- Implementación de una interfaz
- Conectividad con Github
- JSON / API
- Implementar un jenkinsfile

PROBLEMATICA:
Nos contrataron desde la empresa Charlie y la fabrica de chocolate situados en Sevilla para ayudarlos a mejorar su sistema de generación de chocolates. Para esto, el cliente nos dio algunas
especificaciones. Al ser una fabrica de chocolates, tienen en consideración las condiciones climaticas, ya que si la temperatura actual es mayor a 40° no se fabrican
chocolates ese día dado que existe un riesgo de que el mismo se derrita. Si las condiciones climaticas son favorables la producción se hace habitualmente.
Para informar al cliente de como esta yendo el proceso, nos pidio que se lo informemos a traves de un archivo plano. Informandole por producto las cantidades generadas
diariamente. 

ESPECIFICACIONES TECNICAS:
- El programa debe conectar con Github y dejar el codigo en la rama de Desarrollo
- Obtener la información del clima a traves de la API del sitio https://www.el-tiempo.net/
- La interfaz debe implementar el metodo produccionActiva() que es la que indica si ese día se produce chocolate o no.
- Clase Chocolate que herede de la clase Golosina con sus respectivos atributos.
- El jenkinsfile debe mostrar la información por consola de los chocolates generados. 

ENTREGABLES:
- Codigo del proyecto
- Captura de consola de jenkins
- Captura de consola de Java. La consola de Java debe informar si se puede producir chocolate o no, y en el caso de poderse, informar que fue lo que se produjo
el día de hoy. 

SET DE DATOS:
Nombre					CANTIDAD PRODUCIDA
- Chocolate Blanco			1000
- Choclate Negro			1500
- Chocolate con almendras		1200
- Chocolate con castañas de caju	1300
- Chocolate en rama			100
- Chocolate con 70% de cacao		1500
 * 
 * **/