/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type CityPublicApiResource =
  | 'getCitys'
  | 'getById'

export const CityPublicApiResource: ApiResources<CityPublicApiResource> = {
  getCitys:{method: 'get' , url:'/city'},
  getById: { method: 'get', url: '/city/:id' },
};
