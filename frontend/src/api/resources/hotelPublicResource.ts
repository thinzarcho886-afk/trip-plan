/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';
import axios from 'axios';
// define api resource name
type HotelPublicApiResource =
  | 'getHotels'
  | 'getById'

export const hotelPublicApiResource: ApiResources<HotelPublicApiResource> = {
  getHotels:{method: 'get' , url:'/hotel'},
  getById: { method: 'get', url: '/hotel/:id' },
};

