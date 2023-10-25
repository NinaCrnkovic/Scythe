package hr.algebra.scythe.model;



public class Resource {

    enum ResourceType {
        WOOD,
        METAL,
        FOOD;
    }
    private ResourceType resourceType;

    public Resource(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

}
