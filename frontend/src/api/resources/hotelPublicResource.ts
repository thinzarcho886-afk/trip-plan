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

// export const fetchHotelList = async (params: any) => {
//   try {
//     const response = await axios.get('/api/auth/hotel', { params });
//     return response.data; 
//   } catch (error) {
//     console.error("API Error:", error);
//     return { list: [] }; 
//   }
// };