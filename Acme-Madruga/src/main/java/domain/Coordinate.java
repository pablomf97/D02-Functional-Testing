package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Coordinate extends DomainEntity{
	
	private Double latitude;
	private Double longitude;
	
	@NotNull
	@Range(min=-90,max=90)
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@NotNull
	@Range(min=-180,max=180)
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "Latitude: " + latitude + ", Longitude: " + longitude;
	}	

}
