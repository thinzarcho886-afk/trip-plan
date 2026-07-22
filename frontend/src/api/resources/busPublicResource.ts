/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';
import axios from 'axios';
// define api resource name
type BusPublicApiResource =
  | 'getBuses'
  | 'getById'

export const busPublicApiResource: ApiResources<BusPublicApiResource> = {
  getBuses:{method: 'get' , url:'/bus'},
  getById: { method: 'get', url: '/bus/:id' },
};