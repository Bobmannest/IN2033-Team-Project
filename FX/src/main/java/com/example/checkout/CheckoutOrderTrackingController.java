package com.example.checkout;

import com.example.fx.DatabaseConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Controller
@RequestMapping("/api/orders")

//Ai was used for this API call because we needed a html page to display order status
public class CheckoutOrderTrackingController {
    @GetMapping("/track")
    @ResponseBody
    public String trackOrder(@RequestParam String id) {
        String sql = "SELECT order_status FROM OnlineOrder WHERE track_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("order_status");

                return """
                    <html>
                    <head><title>Order Tracking</title></head>
                    <body style="font-family:sans-serif;max-width:600px;margin:40px auto;padding:20px">
                        <h2>Order Tracking</h2>
                        <p><strong>Status:</strong> %s</p>
                        <hr/>
                        <small>Tracking ID: %s</small>
                    </body>
                    </html>
                """.formatted(status, id);

            } else {
                return """
                    <html><body style="font-family:sans-serif;margin:40px auto;max-width:600px">
                        <h2>Order Not Found</h2>
                        <p>No order found for tracking ID: <code>%s</code></p>
                    </body></html>
                """.formatted(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h2>Error</h2><p>Could not retrieve order status.</p></body></html>";
        }
    }
}
