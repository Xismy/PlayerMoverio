
Cliente para reproducir música en otro equipo de la red.

Archivos Java: RemotePlayer/app/src/main/java/com/example/iye19/remoteplayer/

    - Client.java
         Activity que muestra la pantalla de inicio. En ella se muestra una lista con
        los servidores que hay en nuestra red. Para ello hace uso de la clase discover, 
        encargada del descubrimiento de servidores.

    - Discover.java
         Define una AsyncTask. El constructor recibe como argumento un objeto de la clase
        Client. 
         Envía un mensaje de descubrimiento en broadcast y espera las respuestas de los 
        servidores, que responden con un nombre. Crea un ArrayList de objetos de la clase 
        Servidor, que contienen los nombres y las IPs de los servidores.
         Al terminar llama al método update() del objeto client para que se actualice la
        lista de servidores.

    - ItemAdapter.java
         Extiende la clase ArrayAdapter para personalizar los elementos de un ListView de 
        forma que utilicen el layout file. Se utiliza para mostrar los archivos disponibles
        en el servidor.

    - Player.java
         Activity que muestra una pantalla con los archivos del servidor a modo de explorador 
        de archivos. En la parte inferior de la pantalla dispone de controles multimedia para
        reproducir archivos de audio. Al pulsar en los controles se envían mensajes al servidor
        utilizando la clase Request. 

    - Request.java
         AsyncTask utilizada para enviar peticiones al servidor. Al ejecutarlo hay que paserle 
        un Array de Strings de dimension 3. El primer String es la petición. El segundo son los   
        argumentos de la petición en caso de ser necesarios. El tercero es la dirección IP del 
        servidor y sólo es necesario la primera vez que se ejecuta, en la cual se establece la
        conexión.

        Lista de peticiones:
            - ls: lista los archivos del directorio actual.
            - open: cambia el directorio o reproduce el archivo. Utiliza como argumento el índice
            del archivo o directorio. El índice -1 indica el directorio padre.
            - play: para o reanuda la reproducción.
            - set_time: establece el tiempo de la reproducción. Utiliza como argumento el tiempo 
            en ms.

    - Servidor.java
        Representa a un servidor. Contiene un nombre y una IP.