/* VEHICLE RENTAL SYSTEM

GROUP MEMBERS:

1. ANZOA JOSEPHINE CHONO - 25/U/0340 - 2500700340
2. OKWIR FRANCIS - 24/U/10703/EVE - 2400710703
3. NAMUBIRU KIRABO BAKYAITA - 24/U/1038 - 2400701038
4. KISEMBO BANGIRANA RODGERS - 20/U/2036/EVE - 2000702036 */

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        RentalManager manager = new RentalManager();
        manager.loadSampleData();

        SwingUtilities.invokeLater(() -> new RentalSystemGUI(manager).setVisible(true));
    }
}
