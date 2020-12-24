import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Software que, registradas unas preguntas, selecciona aleatoriamente tantas
 * cómo se deseen, de la dificultad que se desee, o mezcladas, indicando la
 * respuesta correcta y llegando a suministrar una nota, ideal para entrevistas.
 * 
 * @author Sergio Jiménez Roncero
 * 
 * @version 3.0.0. Junio 2019.
 *
 */

public class Entrevista_manejando_dificultades_V3 {

	static String[] rutas = { "Configuración de preguntas//Preguntas dificultad FÁCIL.txt",
			"Configuración de preguntas//Preguntas dificultad MEDIA.txt",
			"Configuración de preguntas//Preguntas dificultad DIFÍCIL.txt" };
	static String rutaAleatoria = "Preguntas aleatorias.txt";

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Software creado completamente por Sergio Jiménez Roncero." + "\n\nVersión 3. Junio 2019."
				+ "\n\nBienvenido al programa.\n");

		PrintWriter pw = new PrintWriter(new File(rutaAleatoria));
		for (int i = 0; i < rutas.length; i++) {
			Scanner lector = new Scanner(new File(rutas[i]));
			while (lector.hasNextLine())
				pw.println(lector.nextLine());
			lector.close();
		}
		pw.close();

		menu();

		sc.close();
	}

	public static void menu() {
		System.out.println("Seleccione el modo para mostrar:"
				+ "\n1. Preguntas de dificultad fácil (acierto = +1 punto) (fallo = -3 puntos)"
				+ "\n2. Preguntas de dificultad media (acierto = +2 puntos) (fallo = -2 puntos)"
				+ "\n3. Preguntas de dificultad difícil (acierto = +3 puntos) (fallo = -1 punto)"
				+ "\n4. Preguntas aleatorias de cualquier dificultad (con nota final)." + "\n5. Salir del programa.");

		int opcion = 0;
		boolean repetir;
		do {
			try {
				repetir = false;
				opcion = sc.nextInt();
				if (opcion < 1 || opcion > 5) {
					System.out.println("No es una opción válida. Por favor, vuelva a intentarlo:");
					repetir = true;
				}
			} catch (InputMismatchException excepcion) {
				System.out.println("Debe introducir un caracter numérico. Por favor, vuelva a intentarlo:");
				repetir = true;
				sc.nextLine();
			}
		} while (repetir);

		switch (opcion) {
		case 1:
			System.out.println("Dificultad seleccionada: FÁCIL");
			preguntasTipo(rutas[0], 1);
			break;
		case 2:
			System.out.println("Dificultad seleccionada: MEDIA.");
			preguntasTipo(rutas[1], 2);
			break;
		case 3:
			System.out.println("Dificultad seleccionada: DIFÍCIL.");
			preguntasTipo(rutas[2], 3);
			break;
		case 4:
			System.out.println("Dificultad seleccionada: ALEATORIA.");
			preguntasTipo(rutaAleatoria, 4);
			break;
		case 5:
			System.out.println("Fin del programa.");
			System.exit(1);
		}

		menu(); // Repetición recursiva del programa.
	}

	public static void preguntasTipo(String ruta, int dificultad) {

		try {
			// Conteo de preguntas registradas
			Scanner lector = new Scanner(new File(ruta));
			int contadorPreguntasRegistradas = 0;
			while (lector.hasNext())
				if (lector.next().equals("Pregunta"))
					contadorPreguntasRegistradas++;
			String[] preguntas = new String[contadorPreguntasRegistradas];

			// Conteo de preguntas a mostrar
			System.out.println(
					"\nIntroduzca el número de preguntas aleatorias y diferentes a mostrar, comprendido entre 0 y "
							+ contadorPreguntasRegistradas + " (número de preguntas registradas de esta dificultad):");
			int preguntasAMostrar = 0;
			boolean repetir;
			do {
				try {
					repetir = false;
					preguntasAMostrar = sc.nextInt();
					if (preguntasAMostrar > contadorPreguntasRegistradas || preguntasAMostrar < 0) {
						System.out.println(
								"No hay tantas preguntas diferentes registradas. Por favor, vuelva a intentarlo:");
						repetir = true;
					}
				} catch (InputMismatchException excepcion) {
					System.out.println("Debe introducir un caracter numérico. Por favor, vuelva a intentarlo:");
					repetir = true;
					sc.nextLine();
				}
			} while (repetir);

			lector = new Scanner(new File(ruta)); // Reinicialización de lectura

			// Introducción de preguntas en vector preguntas (tipo String)
			for (int i = 0; lector.hasNextLine(); i++) {
				preguntas[i] = lector.nextLine() + "\n" + lector.nextLine() + "\n" + lector.nextLine();
				lector.nextLine(); // Línea de separación entre preguntas en archivo .txt
			}
			lector.close();
			sc.nextLine(); // Buffer
			int[] preguntasHechas = new int[preguntasAMostrar];
			for (int i = 0; i < preguntasHechas.length; i++)
				preguntasHechas[i] = -1;

			double contadorRespuestasCorrectas = 0;
			for (int i = 0; i < preguntasAMostrar; i++) {
				boolean preguntaRepetida;
				int pregunta;
				do {
					preguntaRepetida = false;
					pregunta = (int) (Math.random() * contadorPreguntasRegistradas);

					for (int j = 0; j < preguntasHechas.length; j++)
						if (preguntasHechas[j] == pregunta)
							preguntaRepetida = true;
				} while (preguntaRepetida);
				System.out.println("\nPregunta nº" + (i + 1) + ":\n" + preguntas[pregunta] + "\n");
				preguntasHechas[i] = pregunta;

				System.out.println("¿Respuesta correcta? (S/s o N/n)");
				String respuesta;
				do {
					repetir = false;
					respuesta = sc.nextLine();
					if (!(respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("N"))) {
						System.out.println(
								"Dato mal introducido. Debe introducir una \"S\" / \"s\" en el caso de que la respuesta haya sido correcta, o una \"N\" / \"n\" si no. Por favor, vuelva a intentarlo:");
						repetir = true;
					}
				} while (repetir);

				if (respuesta.equalsIgnoreCase("S"))
					contadorRespuestasCorrectas++;
			} // fin for

			if (dificultad < 4) {
				int puntosPositivos = (int) contadorRespuestasCorrectas * dificultad;
				int puntosNegativos = -(4 - dificultad) * (int) (preguntasAMostrar - contadorRespuestasCorrectas);
				System.out.println("\nRespuestas correctas: " + (int) contadorRespuestasCorrectas + " (+"
						+ puntosPositivos + " punto/s)." + "\nRespuestas incorrectas: "
						+ (int) (preguntasAMostrar - contadorRespuestasCorrectas) + " (" + puntosNegativos
						+ " punto/s)." + "\nTotal de preguntas: " + preguntasAMostrar
						+ "\nPUNTOS FINALES DE ESTA DIFICULTAD: " + (puntosPositivos + puntosNegativos)
						+ " PUNTOS.\n\n");
			} else {
				System.out.println("\nRespuestas correctas: " + (int) contadorRespuestasCorrectas
						+ "\nRespuestas incorrectas: " + (int) (preguntasAMostrar - contadorRespuestasCorrectas)
						+ "\nTotal de preguntas: " + preguntasAMostrar + "\nPorcentaje de respuestas correctas: "
						+ (100 * (double) contadorRespuestasCorrectas / preguntasAMostrar) + " %" + "\nNota final: "
						+ (10 * (double) contadorRespuestasCorrectas / preguntasAMostrar) + "/10.0\n");
			}

		} catch (FileNotFoundException exc) {
			System.out.println("No se encuentra el archivo de la ruta " + ruta + ".");
		}
	} // fin método

} // fin class