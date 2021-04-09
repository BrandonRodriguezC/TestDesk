package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface GestorDeArchivos {
	
	public static void EscribirArchivo(String Codigo, File ruta) throws IOException {
		PrintWriter writer;
		writer = new PrintWriter(ruta);
		writer.println(Codigo);
		writer.close();
	}

	public static String AbrirArchivo(File ruta) throws IOException {
		String line;
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
