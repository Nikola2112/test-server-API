package com.goit;
import com.goit.sarvlet.WarehouseServlet2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
public class ApplicationStarter {
    public static void main(String[] args) {
        try {
            // Створюємо об'єкт Tomcat
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8090);

            // Створюємо контекст
            Context context = tomcat.addContext("", null); // зміна шляху контексту

            // Додаємо ваш сервлет до контексту
            Tomcat.addServlet(context, "WarehouseServlet2", new WarehouseServlet2());
            context.addServletMappingDecoded("/warehouses", "WarehouseServlet2");

            // Запускаємо Tomcat
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
