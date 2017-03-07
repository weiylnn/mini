package com.qianfan123.minya.network.http.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeilSpears on 2016/10/20.
 */
public class QueryParam {
  private int start;
  private int limit = 10; // 每页默认10条
  private List<FilterParam> filters = new ArrayList<FilterParam>();
  private List<SortParam> sorters = new ArrayList<SortParam>();

  public QueryParam() {
  }

  public QueryParam(int limit) {
    this.limit = limit;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public List<FilterParam> getFilters() {
    return filters;
  }

  public void setFilters(List<FilterParam> filters) {
    this.filters = filters;
  }

  public List<SortParam> getSorters() {
    return sorters;
  }

  public void setSorters(List<SortParam> sorters) {
    this.sorters = sorters;
  }

  public void nextPage() {
    this.start = this.start + this.getLimit();
  }

  public void resetPage() {
    this.start = 0;
  }

  public FilterParam findFilter(String property) {
    for (FilterParam param : getFilters()) {
      if (property.equals(param.getProperty())) {
        return param;
      }
    }
    return null;
  }

  public void remove(String property) {
    for (FilterParam param : getFilters()) {
      if (property.equals(param.getProperty())) {
        getFilters().remove(param);
        return;
      }
    }
  }
}
