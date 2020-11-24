public class GatoFavModel {
    String id;
    String image_id;
    String apikey = "a4c57238-c4d0-4131-b668-25c2863e9e94";
    ImageX image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public ImageX getImage() {
        return image;
    }

    public void setImage(ImageX image) {
        this.image = image;
    }
}
