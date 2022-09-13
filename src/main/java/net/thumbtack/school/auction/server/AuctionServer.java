package net.thumbtack.school.auction.server;

import net.thumbtack.school.auction.SettingsDatabase;
import net.thumbtack.school.auction.rest.mappers.AuctionExceptionMapper;
import net.thumbtack.school.auction.server.config.AuctionResourceConfig;
import net.thumbtack.school.auction.server.config.Settings;
import net.thumbtack.school.auction.service.*;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import net.thumbtack.school.database.mybatis.utils.MyBatisUtils;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuctionServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionServer.class);
    private static Server jettyServer;
    private static boolean setUpIsDone = false;
    private static final UserService userService = new UserService();
    private static final LotService lotService = new LotService();
    private static final BidService bidService = new BidService();
    private static final AuctionService auctionService = new AuctionService();
    private static final ServerService serverService = new ServerService();

    private void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stopServer(null);
            }
        });
    }

    public void startServer(String savedDataFileName){
        if(SettingsDatabase.getTypeOfImpl().equals("mysql")) {
            if (!setUpIsDone) {
                boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();
                if (!initSqlSessionFactory) {
                    throw new RuntimeException("Can't create connection, stop");
                }
                setUpIsDone = true;
            }
        }
        attachShutDownHook();
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(Settings.getRestHTTPPort()).build();
        AuctionResourceConfig config = new AuctionResourceConfig();
        config.register(AuctionExceptionMapper.class);
        jettyServer = JettyHttpContainerFactory.createServer(baseUri, config);
        LOGGER.info("Server started at port " + Settings.getRestHTTPPort());
        serverService.startServer(savedDataFileName);
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
    }

    public void stopServer(String saveDataFileName){
        LOGGER.info("Stopping server");
        try {
            jettyServer.stop();
            jettyServer.destroy();
        } catch (Exception e) {
            LOGGER.error("Error stopping service", e);
            System.exit(1);
        }
        LOGGER.info("Server stopped");

        serverService.stopServer(saveDataFileName);
        /*
        Останавливает сервер и записывает все его содержимое в файл сохранения с именем savedDataFileName.
        Если savedDataFileName == null, запись содержимого не производится.
        */
    }

    public void clearServer() {
        serverService.clearServer();
    }

//    public static void main(String[] args) {
//        attachShutDownHook();
//        startServer(null);
//    }

    public String registerBuyer(String requestJsonString){
        return userService.registerBuyer(requestJsonString);
    }

    public String registerSeller(String requestJsonString){
        return userService.registerSeller(requestJsonString);
    }

    public String loginUser(String requestJsonString){
        return userService.login(requestJsonString);
    }

    public String logoutUser(String requestJsonString){
        return userService.logout(requestJsonString);
    }

    public String deleteUser(String requestJsonString){
        return userService.delete(requestJsonString);
    }

    public String getUserInfo(String requestJsonString) {
        return userService.getUserInfo(requestJsonString);
    }

    public String makeBid(String requestJsonString){
        return bidService.makeBid(requestJsonString);
    }

    public String deleteBid(String requestJsonString){
        return bidService.deleteBid(requestJsonString);
    }

    public String addLot(String requestJsonString){
        return lotService.addLot(requestJsonString);
    }

    public String updateLot(String requestJsonString){
        return lotService.updateLot(requestJsonString);
    }

    public String deleteLot(String requestJsonString){
        return lotService.deleteLot(requestJsonString);
    }

    public String getLots(String requestJsonString){
        return lotService.getLots(requestJsonString);
    }

    public String stopBids(String requestJsonString){
        return lotService.stopBids(requestJsonString);
    }

    public String resumeBids(String requestJsonString) {
        return lotService.resumeBids(requestJsonString);
    }

    public String getLotsByCategory(String requestJsonString){
        return lotService.getLotsByCategory(requestJsonString);
    }

    public String getLotsByCategories(String requestJsonString){
        return lotService.getLotsByCategories(requestJsonString);
    }

    public String getLotsByAtLeastOneCategory(String requestJsonString){
        return lotService.getLotsByAtLeastOneCategory(requestJsonString);
    }

    public String auctionEnd(String requestJsonString){
        return auctionService.auctionEnd(requestJsonString);
    }

}
