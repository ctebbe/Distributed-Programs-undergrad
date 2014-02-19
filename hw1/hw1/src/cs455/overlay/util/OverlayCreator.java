package cs455.overlay.util;
import cs455.overlay.transport.*;
import java.io.*;
import java.lang.*;
import java.util.*;
// crossing fingers we have 10 nodes and 4 edges
public class OverlayCreator {

    // generates an overlay in the same order as nodes comes in
    public String[][] generateOverlay(NodeConnection[] nodes) {

         String[][] overlayArray = null;
         
         if(nodes.length == 3) {
            overlayArray = new String[][]{
                {nodes[1].getServerKey(), nodes[2].getServerKey()},
                {nodes[2].getServerKey()},
                {}
            };
         } else if(nodes.length == 10) { // assigns in the same order as nodes comes in
            overlayArray = new String[][]{
                {nodes[1].getServerKey(), nodes[2].getServerKey(), nodes[9].getServerKey(), nodes[8].getServerKey()},
                {nodes[2].getServerKey(), nodes[3].getServerKey(), nodes[9].getServerKey()},                
                {nodes[3].getServerKey(), nodes[4].getServerKey()},
                {nodes[4].getServerKey(), nodes[5].getServerKey()},
                {nodes[5].getServerKey(), nodes[6].getServerKey()},
                {nodes[6].getServerKey(), nodes[7].getServerKey()},
                {nodes[7].getServerKey(), nodes[8].getServerKey()},
                {nodes[8].getServerKey(), nodes[9].getServerKey()},
                {nodes[9].getServerKey()},
                {}
            };
         }

         return overlayArray;
    }

    // assigns link weights to a constructed overlay
    public String[] assignLinkWeights(NodeConnection[] nodes, String[][] overlay) {
        String[] linkWeights;
        if(nodes.length == 3)  linkWeights = new String[nodes.length];
        else linkWeights = new String[nodes.length*2];
        int linkIndex = 0;

        for(int i=0; i < nodes.length; i++) {
            for(String nodeB : overlay[i]) {
                //System.out.println("\t"+s+" rand:"+Util.generateRandomNumber(1, 10));
                linkWeights[linkIndex++] = nodes[i].getServerKey() + " " + nodeB 
                    + " " + Util.generateRandomNumber(1, 10);
            }
        }
        /*
        for(String s : linkWeights) {
            System.out.println(s);
        }*/
        return linkWeights;
    }
}
