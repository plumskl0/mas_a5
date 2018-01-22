package mas_a5.factories;

import javax.swing.JPanel;
import repast.simphony.relogo.factories.AbstractReLogoPanelCreator;
import mas_a5.relogo.UserGlobalsAndPanelFactory;

public class ReLogoPanelCreator extends AbstractReLogoPanelCreator {

	public void addComponents(JPanel parent) {
		UserGlobalsAndPanelFactory ugpf = new UserGlobalsAndPanelFactory();
		ugpf.initialize(parent);
		ugpf.addGlobalsAndPanelComponents();
	}

}
