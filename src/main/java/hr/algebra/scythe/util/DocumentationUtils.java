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
                        .main-title {
                                color: #ff0000; 
                                text-align: center;
                            }
                        .class-name { color: #008080; }
                        .property-title { color: #800080; }
                        .constructor-title { color: #008000; }
                        .method-title { color: #000080; }
                        h2, h3 { margin: 0; padding: 10px 0; }
                        .section { margin-bottom: 20px; }
                    </style>
                    </head>
                    <body>
                    <h1 class='main-title'>Scythe documentation</h1>
                    """;

            for(String classFilePath : listOfClassFilePaths) {
                String[] pathTokens = classFilePath.split("classes");
                String secondToken = pathTokens[1];
                String fqnWithSlashes = secondToken.substring(1, secondToken.lastIndexOf('.'));
                String fqn = fqnWithSlashes.replace('\\', '.');
                Class<?> deserializedClass = Class.forName(fqn);

                htmlHeader += "<h2 class='class-name'>" + deserializedClass.getSimpleName() + "</h2>\n";



                htmlHeader += "<h3 class='property-title'>Properties</h3>\n";

                Field[] fields = deserializedClass.getDeclaredFields();

                for(Field field : fields) {
                    int modifiers = field.getModifiers();
                    htmlHeader += "<p>";

                    if(Modifier.isPublic(modifiers)) {
                        htmlHeader += "public ";

                    }
                    else if (Modifier.isPrivate(modifiers)) {
                        htmlHeader += "private ";

                    }
                    else if (Modifier.isProtected(modifiers)) {
                        htmlHeader += "protected ";

                    }

                    if(Modifier.isStatic(modifiers)) {
                        htmlHeader += "static ";

                    }

                    if(Modifier.isFinal(modifiers)) {
                        htmlHeader += "final ";

                    }

                    String type = field.getType().getSimpleName();

                    htmlHeader += type + " ";

                    String name = field.getName();

                    htmlHeader += name + "<br>";

                    htmlHeader += "</p>\n";

                }
                htmlHeader += "</div>";

                htmlHeader += "<h3 class='constructor-title'>Constructors</h3>\n";
                Constructor<?>[] constructors = deserializedClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    int modifiers = constructor.getModifiers();

                    htmlHeader += "<p>"; // Start of paragraph for constructor

                    // Add modifiers
                    htmlHeader += Modifier.toString(modifiers) + " ";


                    htmlHeader += deserializedClass.getSimpleName();

                    // Add parameters
                    htmlHeader += "(";
                    Parameter[] parameters = constructor.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        htmlHeader += parameter.getType().getSimpleName() + " " + parameter.getName(); // getName() će sada vratiti pravo ime
                        if (i < parameters.length - 1) {
                            htmlHeader += ", ";
                        }
                    }
                    htmlHeader += ")";

                    htmlHeader += "</p>\n";
                }
                htmlHeader += "</div>\n";


                htmlHeader += "<h3 class='method-title'>Methods</h3>\n";
                Method[] methods = deserializedClass.getDeclaredMethods();
                for (Method method : methods) {
                    int modifiers = method.getModifiers();

                    htmlHeader += "<p>"; // Start of paragraph for method

                    // Add modifiers
                    htmlHeader += Modifier.toString(modifiers) + " ";

                    // Add return type with simple name
                    htmlHeader += method.getReturnType().getSimpleName() + " "; // Use getSimpleName() for return type

                    // Add method name
                    htmlHeader += method.getName();

                    // Add parameters
                    htmlHeader += "(";
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        // Use getSimpleName() for parameter types
                        htmlHeader += parameter.getType().getSimpleName() + " " + parameter.getName();
                        if (i < parameters.length - 1) {
                            htmlHeader += ", ";
                        }
                    }
                    htmlHeader += ")";

                    htmlHeader += "</p>";
                }
                htmlHeader += "</div>";
            }

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

