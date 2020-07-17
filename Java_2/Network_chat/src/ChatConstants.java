public interface ChatConstants {
    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 800;

    final int WINDOW_AUTH_WIDTH = 300;
    final int WINDOW_AUTH_HEIGHT = 150;
    final int MARGIN = 5;
    final int ELEMENT_WIDTH = WINDOW_AUTH_WIDTH - MARGIN * 5;
    final int ELEMENT_HEIGHT = (WINDOW_AUTH_HEIGHT - MARGIN * 6) / 4;
    final String TITLE = "Сетевой чат";
    final String AUTH_TITLE = "Авторизация";
    final String BTN_ENTER = "Отправить";

    final String SERVER_ADDR = "localhost"; // server net name or "127.0.0.1"
    final int SERVER_PORT = 2048; // servet port

    final String AUTH_FAIL = "Авторизация не пройдена";
    final String CONNECT_TO_SERVER = "Соединение с сервером установлено";
    final String NOT_CONNECT_TO_SERVER = "Соединение с сервером не установлено";
    final String CONNECT_CLOSED = "Соединение разорвано";
    final String PVT_MSG = "Приватное сообщение";
    final String UPDATE_LIST = "Обновить список участников";
    final String UPDATE_LIST_MSG = "/command updateList";
    final String BEGIN_OF_NAME = "BEGIN_OF_NAME";
    final String END_OF_NAME = "END_OF_NAME";

}
