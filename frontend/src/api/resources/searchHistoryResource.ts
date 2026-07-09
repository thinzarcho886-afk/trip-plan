import { ApiResources } from '..';

// define api resource name
type SearchHistoryApiResource =
  | 'gethistorys'
  | 'getById'
  | 'getByCityName'
  | 'getByTownshipName'
  | 'getByStreetName'
  | 'getByOwnerName'
  | 'getByHostelName';

const baseUrl = '/auth/search_history';

export const historyApiResource: ApiResources<SearchHistoryApiResource> = {
  gethistorys: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByCityName: { method: 'get', url: baseUrl + '/by-city-name/:cityName' },
  getByTownshipName: { method: 'get', url: baseUrl + '/by-township-name/:townshipName' },
  getByStreetName: { method: 'get', url: baseUrl + '/by-street-name/:streetName' },
  getByOwnerName: { method: 'get', url: baseUrl + '/by-owner-name/:ownerName' },
  getByHostelName: { method: 'get', url: baseUrl + '/by-hostel-name/:hostelName' },
};