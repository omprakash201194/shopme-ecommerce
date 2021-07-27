package com.omprakashgautam.shopme.web.backend.reports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IExporter<T> {
    void export(List<T> entities, HttpServletResponse response) throws IOException;
}
