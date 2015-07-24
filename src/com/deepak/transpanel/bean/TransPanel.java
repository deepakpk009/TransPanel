/* -----------------------------------
 * TransPanel bean v0.2
 * -------------------------------------
 * a  transparent panel bean component for java
 * -------------------------------------
 * Developed By : deepak pk
 * Email : deepakpk009@yahoo.in
 * -------------------------------------
 * This Project is Licensed under GPL
 * -------------------------------------
 *
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.deepak.transpanel.bean;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author deepak
 */
public class TransPanel extends javax.swing.JPanel implements java.beans.Customizer {

    // robot object for capturing screen
    private Robot robot;
    // buffered image object to store captured screen image
    private BufferedImage backgroundImage;
    // background image graphics object for drawing options
    private Graphics backgroundImageGraphics;
    // screen parts and their parameters
    // (x, y) - left-top coordinate
    // width, height - width and heightof the part
    private int part1_x, part1_y, part1_width, part1_height;
    private int part2_x, part2_y, part2_width, part2_height;
    private int part3_x, part3_y, part3_width, part3_height;
    private int part4_x, part4_y, part4_width, part4_height;
    // the desktop screen width
    private int screen_width;
    //the desktop screen height
    private int screen_height;
    // the parent ( JFrame in which the TransPanel is added to) parameters
    // (x, y) - left-top coordinate
    // width, height - width and heightof the parent
    private int parent_x, parent_y, parent_width, parent_height;
    // parent dimension correction value ; default value 80;
    private int errorCorrectionValue = 80; //

    /*
     * method to set the correction value
     */
    public void setErrorCorrectionValue(int errorCorrectionValue) {
        this.errorCorrectionValue = errorCorrectionValue;
    }


    private Object bean;

    /** Creates new customizer TransPanel */
    public TransPanel() {
        initComponents();

        // initialize robot object
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(TransPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        // initilize the background image with the screenshot of the whole desktop screen
        backgroundImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        // initilize the background image graphics object
        backgroundImageGraphics = backgroundImage.getGraphics();
        // ------------ constant value initialisations -------------------------
        // the desktop screen height and width
        screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        // the screen parts constant values initialization
        // part 1
        part1_x = 0;
        part1_y = 0;
        part1_width = screen_width;
        // part 2
        part2_height = this.getHeight();
        // part 3
        part3_height = this.getHeight();
        // part 4
        part4_x = 0;
        part4_width = screen_width;
    }

    /*
     * overriden paint method of the panel to paint the panel with background image
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(backgroundImage.getSubimage(this.getLocationOnScreen().x, this.getLocationOnScreen().y, this.getWidth(), this.getHeight()), 0, 0, this);
        // update parent values
        updateParentValues();
        // update panel values
        updatePanelValues();
        // update background image by parts
        updateBackgroundImageParts();
        // paint all components added to the TransPanel
        this.paintComponents(g);
    }

    /*
     * method to update the parent values
     */
    private void updateParentValues() {
        // do only if the TransPanel is currently showing on screen
        if (this.isShowing()) {
            // calculate the parent values with the error correction
            // the error correction increases the parent dimension
            parent_x = this.getParent().getLocationOnScreen().x - (errorCorrectionValue / 2);
            parent_y = this.getParent().getLocationOnScreen().y - (errorCorrectionValue / 2);
            parent_width = this.getParent().getWidth()
                    + this.getParent().getInsets().left
                    + this.getParent().getInsets().right + errorCorrectionValue;
            parent_height = this.getParent().getHeight()
                    + this.getParent().getInsets().top
                    + this.getParent().getInsets().bottom + errorCorrectionValue;
        }
    }

    /*
     * method to update the panel values
     *
     * the explanation to the below operations is given in the documentatio 'TransPanel-How it works.pdf'
     */
    private void updatePanelValues() {
        // parts value updations
        // part 1
        part1_height = parent_y;
        // part 2
        part2_y = part1_height;
        part2_width = parent_x;
        // part 3
        part3_x = part2_width + parent_width;
        part3_y = part1_height;
        part3_width = screen_width - part3_x;
        // part 4
        part4_y = part1_height + parent_height;
        part4_height = screen_height - part4_y;
    }

    /*
     * update the background image with the screen parts
     */
    private void updateBackgroundImageParts() {
        // update background image
        // part 1 updation
        // do only if the width and height are valid
        if (part1_width > 0 && part1_height > 0) {
            backgroundImageGraphics.drawImage(
                    robot.createScreenCapture(
                    new Rectangle(
                    part1_x, part1_y, part1_width, part1_height)), part1_x, part1_y, this);
        }
        // part 2 updation
        // do only if the width and height are valid
        if (part2_width > 0 && part2_height > 0) {
            backgroundImageGraphics.drawImage(
                    robot.createScreenCapture(
                    new Rectangle(
                    part2_x, part2_y, part2_width, part2_height)), part2_x, part2_y, this);
        }
        // part 3 updation
        // do only if the width and height are valid
        if (part3_width > 0 && part3_height > 0) {
            backgroundImageGraphics.drawImage(
                    robot.createScreenCapture(
                    new Rectangle(
                    part3_x, part3_y, part3_width, part3_height)), part3_x, part3_y, this);
        }
        // part 4 updation
        // do only if the width and height are valid
        if (part4_width > 0 && part4_height > 0) {
            backgroundImageGraphics.drawImage(
                    robot.createScreenCapture(
                    new Rectangle(
                    part4_x, part4_y, part4_width, part4_height)), part4_x, part4_y, this);
        }
    }

    public void setObject(Object bean) {
        this.bean = bean;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                ancestorMovedEvent(evt);
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                ancestorResizedEvent(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /*
     * method called when the parent (JFrame) is moved
     */
    private void ancestorMovedEvent(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_ancestorMovedEvent
        // TODO add your handling code here:
        // paint the TransPanel
        repaint();
    }//GEN-LAST:event_ancestorMovedEvent

    /*
     * method called when the parent (JFrame) is resized
     */
    private void ancestorResizedEvent(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_ancestorResizedEvent
        // TODO add your handling code here:
        // paint the TransPanel
        repaint();
    }//GEN-LAST:event_ancestorResizedEvent


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
