package ism.atelier.atelier.web.dto.request;

public class PlateRequestDto {
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private String model3dUrl;
    private String modelUsdzUrl;
    private Boolean hasAr;
    private String difficulty;
    private String categoryId;

    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getModel3dUrl() { return model3dUrl; }
    public void setModel3dUrl(String model3dUrl) { this.model3dUrl = model3dUrl; }
    public String getModelUsdzUrl() { return modelUsdzUrl; }
    public void setModelUsdzUrl(String modelUsdzUrl) { this.modelUsdzUrl = modelUsdzUrl; }
    public Boolean getHasAr() { return hasAr; }
    public void setHasAr(Boolean hasAr) { this.hasAr = hasAr; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
} 