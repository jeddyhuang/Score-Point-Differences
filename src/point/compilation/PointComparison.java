/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point.compilation;

import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author rxiao
 */
public class PointComparison {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JFileChooser obj1 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        obj1.setDialogTitle("Select the Manually Selected .obj Files:");
        obj1.setMultiSelectionEnabled(true);
        obj1.setAcceptAllFileFilterUsed(false);
        obj1.addChoosableFileFilter(new FileNameExtensionFilter(".obj Files", "obj"));
        if(obj1.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        JFileChooser obj2 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        obj2.setDialogTitle("Select the ICP .obj File:");
        obj2.setAcceptAllFileFilterUsed(false);
        obj2.addChoosableFileFilter(new FileNameExtensionFilter(".obj Files", "obj"));
        if(obj2.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        JFileChooser obj3 = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        obj3.setDialogTitle("Select the Final .obj File:");
        obj3.setAcceptAllFileFilterUsed(false);
        obj3.addChoosableFileFilter(new FileNameExtensionFilter(".obj Files", "obj"));
        if(obj3.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(0);
        
        ArrayList<String> selnames = new ArrayList<String>();
        ArrayList<ObjReader> selobj = new ArrayList<ObjReader>();
        for(int i = 0; i < obj1.getSelectedFiles().length; i ++){
            selnames.add(obj1.getSelectedFiles()[i].getName().substring(0, obj1.getSelectedFiles()[i].getName().length()-4));
            selobj.add(new ObjReader(obj1.getSelectedFiles()[i].getAbsolutePath()));
            selobj.get(i).alphabetizeVertices();
        }
        String icpname = obj2.getSelectedFile().getName().substring(0, obj2.getSelectedFile().getName().length()-4);
        ObjReader icpobj = new ObjReader(obj2.getSelectedFile().getAbsolutePath());
        icpobj.alphabetizeVertices();
        String finname = obj3.getSelectedFile().getName().substring(0, obj3.getSelectedFile().getName().length()-4);
        ObjReader finobj = new ObjReader(obj3.getSelectedFile().getAbsolutePath());
        finobj.alphabetizeVertices();
        
        JFileChooser targetfolder = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        targetfolder.setDialogTitle("Where to Save your File:");
        targetfolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(targetfolder.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            String SEL_dir = targetfolder.getSelectedFile().getAbsolutePath() + "\\" + finname + " Comparison.csv";
            PrintWriter writer = new PrintWriter(SEL_dir, "UTF-8");
            writer.print(finname + ",");
            for(int i = 0; i < selnames.size(); i ++) writer.print(finname + " + " + selnames.get(i) + ",");
            writer.print(finname + " + " + icpname + ",");
            for(int i = 0; i < selnames.size(); i ++) for(int j = i + 1; j < selnames.size(); j ++) writer.print(selnames.get(i) + " + " + selnames.get(j) + ",");
            for(int i = 0; i < selnames.size(); i ++) writer.print(icpname + " + " + selnames.get(i) + ",");
            writer.println();
            for(int i = 0; i < finobj.compileVertices().size(); i ++){
                writer.print(finobj.compileVertices().get(i).getname() + ",");
                for(int j = 0; j < selobj.size(); j ++) writer.print(finobj.compileVertices().get(i).distance(selobj.get(j).compileVertices().get(i)) + ",");
                writer.print(finobj.compileVertices().get(i).distance(icpobj.compileVertices().get(i)) + ",");
                for(int j = 0; j < selobj.size(); j ++) for(int k = j + 1; k < selobj.size(); k ++) writer.print(selobj.get(j).compileVertices().get(i).distance(selobj.get(k).compileVertices().get(i)) + ",");
                for(int j = 0; j < selobj.size(); j ++) writer.print(selobj.get(j).compileVertices().get(i).distance(icpobj.compileVertices().get(i)) + ",");
                writer.println();
            }
            writer.close();
        }
    }
}
