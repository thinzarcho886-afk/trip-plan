/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type StreetPublicApiResource =
  | 'getStreets'
  | 'getById'

export const streetPublicApiResource: ApiResources<StreetPublicApiResource> = {
  getStreets:{method: 'get' , url:'/street'},
  getById: { method: 'get', url: '/street/:id' },
};

