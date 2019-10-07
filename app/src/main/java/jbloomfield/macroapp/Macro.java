package jbloomfield.macroapp;

/**
 * Stores a name and macro pair
 */

public class Macro {
    // identfier for the macro
    private String name;
    // contains the simple autohotkey script to accomplish the task
    private String macro;



    Macro(String name, String macro){
        this.name = name;
        this.macro = macro;

    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getMacro() {
        return macro;
    }

    public void setMacro(String macro) {
        this.macro = macro;
    }


}
