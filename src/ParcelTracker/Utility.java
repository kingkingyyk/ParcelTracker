package ParcelTracker;

import java.awt.Image;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import javax.swing.ImageIcon;

public class Utility {

    public static ImageIcon resizeImageIcon (ImageIcon ic, int width, int height) {
    	return new ImageIcon(ic.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

}
