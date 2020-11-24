import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GatitosServicio {
    public static void verGatos() throws IOException {
        //traer datos de la api
        //OkHttpClient client = new OkHttpClient().newBuilder().build();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        //este codigo se comunica con algo externo, excep de entrada y salida
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();
        //cortar el primer caracter y ultimo las llaves
        elJson = elJson.substring(1, elJson.length()-1);

        //parsear para tener un obj tipo gato, crear objeto de la clase gson
        Gson gson = new Gson();
        //convertir la rspta de la api a tipo gato
        GatosModel gatos = gson.fromJson(elJson, GatosModel.class);

        //redimensionar la imagen si necesita
        Image image = null;

        try {
            //convertir en image icon
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            //solve? otro opcion api https://dog.ceo/dog-api/
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            //search
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            ImageIcon fondoG = new ImageIcon(bufferedImage);

            //JOptionPane recibe una img de tipo icono
            ImageIcon fondoGato = new ImageIcon(image);

            if(fondoGato.getIconWidth() >800 ){
                //redimensionar
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String menu = "Opciones: \n"
                    + "1. Ver otra imagen \n"
                    + "2. Favorito \n"
                    + "3. Volver al menu \n";

            String[] botones = {"Ver otra imagen", "Favorito", "Volver"};
            //String id_gato = String.valueOf(gatos.getId()); //volviendolo a string para ver en la interfaz graf
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato,
                    JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

            int seleccion = -1;
            for (int i = 0; i < botones.length; i++) {
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }

            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void favoritoGato(GatosModel gato){
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+gato.getId()+"\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void verFavoritos(String apiKey){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("GET", null)
                    .addHeader("x-api-key", apiKey)
                    .build();
            Response response = client.newCall(request).execute();

            //Guardamos el string con la rspta
            String elJson = response.body().string();
            //creando un objeto Gson
            Gson gson = new Gson();

            GatoFavModel[] gatosArray = gson.fromJson(elJson, GatoFavModel[].class);

            if(gatosArray.length > 0){
                int min = 1;
                int max = gatosArray.length;
                int aleatorio = (int) (Math.random()*((max - min)+1)) + min;
                int indice = aleatorio - 1;

                GatoFavModel gatofav = gatosArray[indice];

                //copiado de la linea 34
                //redimensionar la imagen si necesita
                Image image = null;

                try {
                    //convertir en image icon
                    URL url = new URL(gatofav.image.getUrl());
                    image = ImageIO.read(url);

                    //solve? otro opcion api https://dog.ceo/dog-api/
                    HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
                    //search
                    httpcon.addRequestProperty("User-Agent", "");
                    BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                    ImageIcon fondoG = new ImageIcon(bufferedImage);

                    //JOptionPane recibe una img de tipo icono
                    ImageIcon fondoGato = new ImageIcon(image);

                    if(fondoGato.getIconWidth() >800 ){
                        //redimensionar
                        Image fondo = fondoGato.getImage();
                        Image modificada = fondo.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                        fondoGato = new ImageIcon(modificada);
                    }

                    String menu = "Opciones: \n"
                            + "1. Ver otra imagen \n"
                            + "2. Eliminar favorito \n"
                            + "3. Volver al menu \n";

                    String[] botones = {"Ver otra imagen", "Eliminar favorito", "Volver"};
                    //String id_gato = String.valueOf(gatos.getId()); //volviendolo a string para ver en la interfaz graf
                    String id_gato = gatofav.getId();
                    String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato,
                            JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

                    int seleccion = -1;
                    for (int i = 0; i < botones.length; i++) {
                        if(opcion.equals(botones[i])){
                            seleccion = i;
                        }
                    }

                    switch (seleccion){
                        case 0:
                            verFavoritos(apiKey);
                            break;
                        case 1:
                            borrarFavorito(gatofav);
                            break;
                        default:
                            break;
                    }

                }catch (IOException e){
                    System.out.println(e);
                }

            }


        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void borrarFavorito(GatoFavModel gatoFav){
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+gatoFav.getId()+"")
                    .method("DELETE", body)
                    .addHeader("x-api-key", gatoFav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

            //coment
            if(response.code() == 200) {
                JOptionPane.showMessageDialog(null, "Gato Favorito " + gatoFav.getId() + " Eliminado ");
            }else {
                JOptionPane.showMessageDialog(null, "Algo fallo " + response.code());
            }

        }catch (IOException e){
            System.out.println(e);
        }
    }
}
