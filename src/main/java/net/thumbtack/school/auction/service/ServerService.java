package net.thumbtack.school.auction.service;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.dao.DatabaseDao;

public class ServerService {
    private final DatabaseDao databaseDao = SettingsDatabase.getDatabaseDao();

    public void startServer(String savedDataFileName){
        /*
        Производит всю необходимую инициализацию и запускает сервер.
        savedDataFileName - имя файла, в котором было сохранено состояние сервера.
        Если savedDataFileName == null, восстановление состояния не производится, сервер стартует “с нуля”.
        */
        /*
        Список всех категорий загружается в базу данных при инициализации сервера из текстового файла,
        в котором название каждой категории находится в отдельной строке.
        Во время работы сервера список категорий изменению не подлежит.
        */
        databaseDao.clear();
        //Как сделать считывание файла с mysql ?
//        DatabaseDao databaseDao = Settings.getDatabaseDao();
//        if (savedDataFileName != null) {
//            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(savedDataFileName)), StandardCharsets.UTF_8))){
//                String string = br.readLine();
//                databaseDao.loadFromDatabase(string);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void stopServer(String saveDataFileName){
        /*
        Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
        Если savedDataFileName == null, запись содержимого не производится.
        */
//        if(saveDataFileName != null){
//            File file = new File(saveDataFileName);
//            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))){
//                file.createNewFile();
//                bw.write(new Gson().toJson(Database.getInstance()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        databaseDao.clear();
    }

    public void clearServer() {
        databaseDao.clear();
    }

}
