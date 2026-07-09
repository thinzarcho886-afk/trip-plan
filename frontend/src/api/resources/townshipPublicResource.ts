/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type TownshipPublicApiResource =
  | 'getTownships'
  | 'getById'

export const townshipPublicApiResource: ApiResources<TownshipPublicApiResource> = {
  getTownships:{method: 'get' , url:'/township'},
  getById: { method: 'get', url: '/township/:id' },
};

