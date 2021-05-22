package hmjc.hm3.hm32;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        StringBuffer path = new StringBuffer("C:\\javahm\\Games");
        path.append("\\savegames\\");
        List<String> paths = new ArrayList<>();
        paths.add(path + "save1.dat");
        paths.add(path + "save2.dat");
        paths.add(path + "save3.dat");

        GameProgress gameProgress1 = new GameProgress(100, 100, 100, 100.0);
        GameProgress gameProgress2 = new GameProgress(200, 200, 200, 200.0);
        GameProgress gameProgress3 = new GameProgress(30, 30, 30, 33.3);

        saveGame(paths.get(0), gameProgress1);
        saveGame(paths.get(1), gameProgress2);
        saveGame(paths.get(2), gameProgress3);

        if (zipFiles(path+"savesInZip.zip", paths)){
            clearAfterZip(paths);
        }




    }

    static boolean saveGame(String path, GameProgress gameProgress) {
        File file = new File(path);
        System.out.println(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream bos = new ObjectOutputStream(fos)) {

                bos.writeObject(gameProgress);
                System.out.println("Запись прошла успешно");

            } catch (Exception ex) {
                System.out.println("ошибка записи в файл : " + ex.getMessage());
                return false;
            }

        } catch (Exception ex) {
            System.out.println("ошибка сохранения : " + ex.getMessage());
            return false;
        }


        return true;
    }

    static boolean zipFiles(String pathToZip, List<String> files) {
        File zipFile = new File(pathToZip);
        try {
            if (!zipFile.exists()) {
                if (!zipFile.createNewFile()) {
                    System.out.println("Создание файла не удалось");
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.println("что-то пошло не так c созданием файла : " + ex.getMessage());
            return false;
        }
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String filename : files) {

                try (FileInputStream fis = new FileInputStream(filename)) {
                    File f = new File(filename);
                    ZipEntry zipEntry = new ZipEntry(f.getName());
                    zout.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    System.out.println("файл " + f.getName() + " добавлен в архив");
                } catch (Exception ex) {
                    System.out.println("что-то пошло не так добавлением в архив : " + ex.getMessage());
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.println("что-то пошло не так c архивом : " + ex.getMessage());
            return false;
        }
        System.out.println("запись в архив прошла успешно");
        return true;
    }

    static boolean clearAfterZip(List<String> paths){
        boolean allDeleted = true;
        for (String filename: paths) {
            File file = new File(filename);
            if (file.delete()){
                System.out.println(filename + " удалён");
            }else {
                System.out.println("не удалось удалить " + filename);
                allDeleted = false;

            }
        }
        return allDeleted;
    }

}
