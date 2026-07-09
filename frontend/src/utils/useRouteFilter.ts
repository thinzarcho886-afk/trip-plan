import { reactive, toRefs } from 'vue';

interface RouteFilterResult<T> {
  isFiltered: boolean;
  filteredValues: string;
  filterCount: number;
  routeFilter: T | any;
}

export function useRouteFilter<T>(searchFilter: any, routeQuery: any) {
  const result = reactive<RouteFilterResult<T>>({
    isFiltered: false,
    filteredValues: '',
    filterCount: 0,
    routeFilter: {},
  });

  if (routeQuery && searchFilter) {
    Object.keys(searchFilter).forEach((key) => {
      result.routeFilter[key] = '';

      if (
        routeQuery[key] != undefined &&
        routeQuery[key] != null &&
        String(routeQuery[key]) != ''
      ) {
        result.routeFilter[key] = routeQuery[key];

        result.isFiltered = true;
        result.filterCount++;
        result.filteredValues += `${
          result.filteredValues == '' ? '' : ', '
        }${key}:${routeQuery[key]}`;
      }
    });
  }

  return { ...toRefs(result) };
}
