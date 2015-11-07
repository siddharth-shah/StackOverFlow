package app.stackexchange.siddharthshah.myapplication.model;

/**
 * Created by siddharthshah on 07/11/15.
 */
public class ApiExtraParams {
    public ApiExtraParams(String page, String pageSize, String sortCriteria, String filter, String order, String siteToSearch) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortCriteria = sortCriteria;
        this.filter = filter;
        this.order = order;
        this.siteToSearch = siteToSearch;
    }

    String page;
    String siteToSearch;
    String pageSize;
    String sortCriteria;
    String filter;
    String order;

    public String getPage() {
        return page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public String getFilter() {
        return filter;
    }

    public String getOrder() {
        return order;
    }

    public String getSiteToSearch() {
        return siteToSearch;
    }
}
