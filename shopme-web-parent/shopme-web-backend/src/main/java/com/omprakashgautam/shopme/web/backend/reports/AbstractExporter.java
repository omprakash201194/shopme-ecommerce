package com.omprakashgautam.shopme.web.backend.reports;

import com.omprakashgautam.shopme.commons.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author omprakash gautam
 * Created on 15-Jul-21 at 7:51 PM.
 */
public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "users_" + timestamp + extension;
        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename =" + fileName;
        response.setHeader(headerKey, headerValue);
    }

}
