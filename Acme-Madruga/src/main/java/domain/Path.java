
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.PROPERTY)
public class Path extends DomainEntity {

	private Collection<Segment>	segments;


	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Segment> getSegments() {
		return this.segments;
	}

	public void setSegments(final Collection<Segment> segments) {
		this.segments = segments;
	}

}
