package software.ulpgc.view.display;

public interface MainFrame {
    void add(PanelDisplay panel, String position);
    void show();
    record WindowSize(int x, int y){
        public WindowSize verticalSplit(){
            return new WindowSize(this.x/2,this.y);
        }
    }
}
