package hr.algebra.scythe.util;


import javafx.scene.control.Alert;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DocumentationUtils {

    public static void generateHtmlDocumentationFile() {

        try {
            List<String> listOfClassFilePaths = Files.walk(Paths.get("target"))
                    .map(Path::toString)
                    .filter(f -> f.endsWith(".class"))
                    .filter(f -> !f.endsWith("module-info.class"))
                    .toList();

            String htmlHeader = """
    <!DOCTYPE html>
    <html>
    <head>
    <title>Project Documentation</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .class-name { color: #008080; }
        .property-title { color: #800080; }
        .constructor-title { color: #008000; }
        .method-title { color: #000080; }
        h1, h2 { margin: 0; padding: 10px 0; }
        .section { margin-bottom: 20px; }
    </style>
    </head>
    <body>
    """;

            for(String classFilePath : listOfClassFilePaths) {
                String[] pathTokens = classFilePath.split("classes");
                String secondToken = pathTokens[1];
                String fqnWithSlashes = secondToken.substring(1, secondToken.lastIndexOf('.'));
                String fqn = fqnWithSlashes.replace('\\', '.');
                Class<?> deserializedClass = Class.forName(fqn);

                htmlHeader += "<h1 class='class-name'>" + deserializedClass.getSimpleName() + "</h1>\n";



                htmlHeader += "<h2 class='property-title'>Properties</h2>\n";

                Field[] fields = deserializedClass.getDeclaredFields();

                for(Field field : fields) {
                    int modifiers = field.getModifiers();

                    if(Modifier.isPublic(modifiers)) {
                        htmlHeader += "public ";
                        //System.out.print("public ");
                    }
                    else if (Modifier.isPrivate(modifiers)) {
                        htmlHeader += "private ";
                        //System.out.print("private ");
                    }
                    else if (Modifier.isProtected(modifiers)) {
                        htmlHeader += "protected ";
                        //System.out.print("protected ");
                    }

                    if(Modifier.isStatic(modifiers)) {
                        htmlHeader += "static ";
                        //System.out.print("static ");
                    }

                    if(Modifier.isFinal(modifiers)) {
                        htmlHeader += "final ";
                        //System.out.print("final ");
                    }

                    String type = field.getType().getSimpleName();

                    htmlHeader += type + " ";

                    String name = field.getName();

                    htmlHeader += name + "<br>";
                    //System.out.println(name + " ");

                }
                htmlHeader += "</div>";

                // Kod unutar vaše for petlje, prije ili nakon ispisivanja metoda

                htmlHeader += "<h2 class='constructor-title'>Constructors</h2>\n";
                Constructor<?>[] constructors = deserializedClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    int modifiers = constructor.getModifiers();

                    htmlHeader += "<p>"; // Početak paragrafa za konstruktor

                    // Dodajemo modifikatore
                    htmlHeader += Modifier.toString(modifiers) + " ";

                    // Dodajemo ime konstruktora (samo ime klase)
                    htmlHeader += deserializedClass.getSimpleName();

                    // Dodajemo parametre
                    htmlHeader += "(";
                    Parameter[] parameters = constructor.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        htmlHeader += parameter.getType().getTypeName() + " " + parameter.getName();
                        if (i < parameters.length - 1) {
                            htmlHeader += ", ";
                        }
                    }
                    htmlHeader += ")";

                    htmlHeader += "</p>\n"; // Kraj paragrafa za konstruktor
                }
                htmlHeader += "</div>\n";

                htmlHeader += "<h2 class='method-title'>Methods</h2>\n";
                Method[] methods = deserializedClass.getDeclaredMethods();
                for (Method method : methods) {
                    int modifiers = method.getModifiers();

                    htmlHeader += "<p>"; // Početak paragrafa za metodu

                    // Dodajemo modifikatore
                    htmlHeader += Modifier.toString(modifiers) + " ";

                    // Dodajemo povratni tip
                    htmlHeader += method.getReturnType().getTypeName() + " ";

                    // Dodajemo ime metode
                    htmlHeader += method.getName();

                    // Dodajemo parametre
                    htmlHeader += "(";
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        htmlHeader += parameter.getType().getTypeName() + " " + parameter.getName();
                        if (i < parameters.length - 1) {
                            htmlHeader += ", ";
                        }
                    }
                    htmlHeader += ")";

                    htmlHeader += "</p>"; // Kraj paragrafa za metodu
                }


            }
            htmlHeader += "</div>";

            String htmlFooter = """
                </body>
                </html>
                """;

            Path htmlDocumentationFile = Path.of("files/documentation.html");

            String fullHtml = htmlHeader + htmlFooter;

            Files.write(htmlDocumentationFile, fullHtml.getBytes());
            DialogUtils.showDialog(Alert.AlertType.INFORMATION, "File created!",
                    "Creation of HTML documentation file succeeded!");
        } catch (IOException | ClassNotFoundException e) {
            DialogUtils.showDialog(Alert.AlertType.INFORMATION, "File not created!",
                    "Creation of HTML documentation file failed!");
        }
    }

}

