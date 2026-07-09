import { ApiResources } from '..';

// define api resource name
type SearchHistoryPublicApiResource =
  |'register'
  |'getMostSearchedHostels'
  |'gethistorys';

const baseUrl = '/search_history';

export const historyPublicApiResource: ApiResources<SearchHistoryPublicApiResource> = {
  register: { method: 'post', url: baseUrl },
  getMostSearchedHostels:{ method: 'get', url: baseUrl+'/filter' },
  gethistorys:{ method: 'get', url: baseUrl },
  };