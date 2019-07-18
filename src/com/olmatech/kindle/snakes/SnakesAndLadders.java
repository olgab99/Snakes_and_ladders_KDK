package com.olmatech.kindle.snakes;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;

public class SnakesAndLadders extends AbstractKindlet {
	private Controller controller;

    public void create(final KindletContext context) {
       // final JLabel label = new JLabel("Hello World", JLabel.CENTER);
       // context.getRootContainer().add(label, BorderLayout.CENTER);
    	//JPanel startPanel = new StartPanel("start.png");
    	//context.getRootContainer().add(startPanel);
    	controller = Controller.getController();
    	controller.initUI(context);
    }

    public void start() {
        controller.onStart();
    }    

    public void stop() {
        controller.onStop();
    }    

    public void destroy() {
    	controller.onDestroy();
    }
}
