# TestDesk v 1.0.2
Version de prueba de TestDesk: Software de aprendizaje de algoritmos estructurados

# Descripción

TestDesk es un software de aprendizaje de algoritmos estructurados para principantes
o estudiantes que apenas empiezan a programar, este software se comportara como un IDE (Entorno de desarrollo integrado) el cual te ayudara a programar tus primeros algoritmos utilizando un pseudocodigo en español. 

**Principales características:**
 - Desarrollo/inserción de estructuras de pseudocodigo mediante un menu de generacion de bloques
 - Filtro de teclado en el documepnto para campos no requeridos
 - Analizador léxico, sintáctico y semántico de las diferentes expresiones
 - Notificación visual de errores en area de codificación
 - Notificación detalllada de errores en consola
 - Ejecución pausada (paso a paso)
 - Generación de tablas de prueba de escritorio (manual y automatica) perfiladas con las lineas de codigo
 - Registro de cambios en variables en tabla de prueba de escritorio automatica (algoritmo en ejecución)
 - Almacenamiento de instancias de documentos (pseudocodigo fuente) ~ Guardar codigo/archivo
 - Apertura de instancias de documentos (pseudocodigo fuente) ~ Abrir codigo/archivo
 - Generación de multiples areas de codigo

**Screenshot**: Está es una repesentación de ejemplo del aplicativo; e.j.,

![](https://64.media.tumblr.com/53ca0f6f0419506226b5e1b4b8135cd0/d1130cd7866bb319-77/s2048x3072/290faa4f938c47b667f0a80a49400d4ac4a70ea3.png)

## Instalación

Para instalar, simplemente descarga el *.jar* y empieza a usarlo **No habilitado por ahora** <([descarga](https://www.google.com/))>

----
## Uso

* Sobre el area de codigo has click izquierdo y encontraras el menu de inserción de estructuras de codigo.
* Unicamente podras escribir con el teclado sobre las areas sombreadas
* El programa te sombreará la expresion de color amarillo en caso de que la entrada tenga un error ya sea lexicamente, sintacticamente o semanticamente  
* Para saber mas detalle del error, has click en el panel derecho en la pestaña "Consola de errores"
* Para ejecutar tu codigo, has click en el botón *Secuenciar* y luego has click en el botón *Ejecutar siguiente* para ejecutar la siguiente instrucción (ambos botones se encuentran en la barra superior)
* Los cambios se verán efectuados en el panel central en la pestaña *Tabla Automatica*
* Para guardar el archivo has click en el botón *Guardar archivo* (ubicado en la barra superior)
* Para abrir un archivo has click en el botón *Abrir archivo* (ubicado en la barra superior)
* Si deseas utilizar otro editor de codigo en paralelo has click en el botón *Nueva pestaña* (ubicado en la barra superior)

## Errores conocidos

- Evaluación de expresiones inoperables (e.j 1/0)
- Insercion de bloques en areas no validas/coherentes
- Indentación equivoca (numero de tabulaciones incorrecto) >ocacional<

## Obteniendo ayuda

Aún se encuentra en desarrollo, por tanto esta función se habilitará en un futuro

----

## Entorno técnico

  - **Lenguaje**: Java SE 8 [jdk 1.8.0_261].
  - **Entorno grafico**:  JavaFX
  - **Dependencias**:
    * javafx
    * org.eclipse.fx.ui.controls.styledtext
    * java.util

### Sets de pruebas de ejecución
Actualmente las pruebas más vitales para la ejecución son Condicional.td y tablas-de-multiplicar.td (revisar en [carpeta](https://github.com/BrandonRodriguezC/TestDesk/tree/main/Pruebas))

## Commit

### Cambios
- [ ] Ajustar estructura de declaracion por *tipo nombre;* o *tipo nombre, nombre, nombre;* 
- [X] Cambio de keyword *decimal* por *real*
- [X] Validacion de enteros y reales en tipo de dato *decimal/real*
- [X] Cambio de keywords *true | false* por *veradero | falso*
- [X] Ejecucion paso a paso 

### Por hacer
* Lectura de consola en ejecucion
* Escritura de consola en ejecucion
* Colores en errores e información
* Revisar en evaluador el operador *=!* por *!=* 
* Revisar operaciones (implementar *try and catch* para expresiones inoperables en ejecucion ~ e.j 1/0)

----

## Creditos and referencias 

1. Giovanni Fajardo (co-autor)
2. Brandon Rodriguez (autor)
3. Documentacion:
    - ALFONSECA MORENO, Manuel et al. Compiladores e intérpretes: teoría y práctica. Madrid: PEARSON PRENTICE HALL, 2006. 376p. ISBN 84-205-5031-0
    - UNIVERSITAT JAUME I. Procesadores de Lenguaje: Estructura de los compiladores e interpretes. En: Universidad Jaume I de Castellón [en línea]. (2010-2011). Disponible en: <http://repositori.uji.es/xmlui/bitstream/handle/10234/22656/II26_estructura_compiladores.pdf?sequence=1>. [citado en 1 de noviembre de 2020] 
    - Stackoverflow :P
    - Multiples foros de desarrollo