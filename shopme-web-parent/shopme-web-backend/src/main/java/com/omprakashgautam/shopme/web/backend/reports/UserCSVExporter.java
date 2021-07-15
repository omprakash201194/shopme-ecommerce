package com.omprakashgautam.shopme.web.backend.reports;

import com.omprakashgautam.shopme.commons.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author omprakash gautam
 * Created on 15-Jul-21 at 7:18 PM.
 */
public class UserCSVExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "users_" + timestamp + ".csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + fileName;

        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String [] csvHeader = {"User Id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String [] fieldMapping = {"id","email","firstName", "lastName", "roles","enabled"};


        csvBeanWriter.writeHeader(csvHeader);
        for (User user : listUsers) {
            csvBeanWriter.write(user,fieldMapping);
        }
        csvBeanWriter.close();


    }
}
