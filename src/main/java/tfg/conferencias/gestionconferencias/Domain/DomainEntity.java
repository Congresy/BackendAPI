package tfg.conferencias.gestionconferencias.Domain;


import org.springframework.data.annotation.Id;

public abstract class DomainEntity {

    // Constructors -----------------------------------------------------------

    public DomainEntity() {
        super();
    }


    // Identification ---------------------------------------------------------
    @Id
    private String	id;
    @org.springframework.data.annotation.Version
    private int	version;



    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }


    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    // Object interface -------------------------------------------------------

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }//

    @Override
    public boolean equals(final Object other) {
        boolean result;

        if (this == other)
            result = true;
        else if (other == null)
            result = false;
        else if (other instanceof Integer)
            result = (this.getId() == (String) other);
        else if (!this.getClass().isInstance(other))
            result = false;
        else
            result = (this.getId() == ((DomainEntity) other).getId());

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result;

        result = new StringBuilder();
        result.append(this.getClass().getName());
        result.append("{");
        result.append("id=");
        result.append(this.getId());
        result.append(", version=");
        result.append(this.getVersion());
        result.append("}");

        return result.toString();
    }
}
