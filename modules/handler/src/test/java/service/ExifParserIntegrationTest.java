package service;

import com.lazyants.filecessor.Application;
import com.lazyants.filecessor.configuration.ApplicationConfiguration;
import com.lazyants.filecessor.configuration.BaseApplicationConfiguration;
import com.lazyants.filecessor.model.Exif;
import com.lazyants.filecessor.service.ExifParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ExifParserIntegrationTest {

    @Autowired
    private ApplicationConfiguration configuration;

    @Test
    public void test_parseExif_nikon() {
        ExifParser parser = new ExifParser(configuration.getExiftoolPath());
        Exif exif = parser.parseExif(new File("src/test/resources/images/nikon.jpg"));
        assertEquals(exif.getExposureTime(), "1/125");
        assertEquals(exif.getAperture(), "4.0");
        assertEquals(exif.getFocalLength(), "24.0 mm");
        assertEquals(exif.getPixelXDimension(), 240.0);
        assertEquals(exif.getPixelYDimension(), 240.0);
        assertEquals(exif.getWidth(), 2512);
        assertEquals(exif.getHeight(), 3364);
        assertEquals(exif.getIso(), "800");
        assertEquals(exif.getCamera(), "NIKON D3");
        assertEquals(exif.getLens(), "AF-S Zoom-Nikkor 24-70mm f/2.8G ED");
        assertEquals(exif.getDatetimeOriginal(), new GregorianCalendar(2010, 7, 29, 14, 3, 36).getTime());
    }

    @Test
    public void test_parseExif_canon() {
        ExifParser parser = new ExifParser(configuration.getExiftoolPath());
        Exif exif = parser.parseExif(new File("src/test/resources/images/canon.jpg"));
        assertEquals(exif.getExposureTime(), "8");
        assertEquals(exif.getAperture(), "8.0");
        assertEquals(exif.getFocalLength(), "20.0 mm");
        assertEquals(exif.getColorSpace(), "sRGB");
        assertEquals(exif.getPixelXDimension(), 72.0);
        assertEquals(exif.getPixelYDimension(), 72.0);
        assertEquals(exif.getWidth(), 5184);
        assertEquals(exif.getHeight(), 3456);
        assertEquals(exif.getIso(), "100");
        assertEquals(exif.getCamera(), "Canon EOS Kiss X7");
        assertEquals(exif.getLens(), "Canon EF-S 10-22mm f/3.5-4.5 USM");
        assertEquals(exif.getDatetimeOriginal(), new GregorianCalendar(2013, 1, 6, 18, 9, 49).getTime());
    }

    @Test
    public void test_parseExif_sony() {
        ExifParser parser = new ExifParser(configuration.getExiftoolPath());
        Exif exif = parser.parseExif(new File("src/test/resources/images/sony.jpg"));
        assertEquals(exif.getExposureTime(), "1/320");
        assertEquals(exif.getFocalLength(), "10.4 mm");
        assertEquals(exif.getColorSpace(), "sRGB");
        assertEquals(exif.getPixelXDimension(), 600.0);
        assertEquals(exif.getPixelYDimension(), 600.0);
        assertEquals(exif.getWidth(), 800);
        assertEquals(exif.getHeight(), 667);
        assertEquals(exif.getIso(), "0");
        assertEquals(exif.getCamera(), "DSC-QX100");
        assertEquals(exif.getDatetimeOriginal(), new GregorianCalendar(2014, 8, 14, 11, 26, 12).getTime());
    }

    @Test
    public void test_parseExif_android() {
        ExifParser parser = new ExifParser(configuration.getExiftoolPath());
        Exif exif = parser.parseExif(new File("src/test/resources/images/android.jpg"));
        assertEquals(exif.getExposureTime(), "1/33");
        assertEquals(exif.getAperture(), "1.9");
        assertEquals(exif.getFocalLength(), "4.3 mm");
        assertEquals(exif.getColorSpace(), "sRGB");
        assertEquals(exif.getPixelXDimension(), 72.0);
        assertEquals(exif.getPixelYDimension(), 72.0);
        assertEquals(exif.getWidth(), 5312);
        assertEquals(exif.getHeight(), 2988);
        assertEquals(exif.getIso(), "50");
        assertEquals(exif.getCamera(), "SM-G920F");
        assertEquals(exif.getDatetimeOriginal(), new GregorianCalendar(2014, 0, 4, 11, 59, 41).getTime());
    }

    @Test
    public void test_parseExif_iphone() {
        ExifParser parser = new ExifParser(configuration.getExiftoolPath());
        Exif exif = parser.parseExif(new File("src/test/resources/images/iphone.jpg"));
        assertEquals(exif.getExposureTime(), "1/145");
        assertEquals(exif.getAperture(), "2.2");
        assertEquals(exif.getFocalLength(), "4.2 mm");
        assertEquals(exif.getColorSpace(), "sRGB");
        assertEquals(exif.getPixelXDimension(), 72.0);
        assertEquals(exif.getPixelYDimension(), 72.0);
        assertEquals(exif.getWidth(), 3264);
        assertEquals(exif.getHeight(), 2448);
        assertEquals(exif.getIso(), "32");
        assertEquals(exif.getLatitude(), 36.72960555555556);
        assertEquals(exif.getLongitude(), 30.56276666666667);
        assertEquals(exif.getCamera(), "iPhone 6");
        assertEquals(exif.getDatetimeOriginal(), new GregorianCalendar(2015, 8, 30, 21, 31, 4).getTime());
    }
}
