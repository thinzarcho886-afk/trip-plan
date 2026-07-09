/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';
import axios from 'axios';
// define api resource name
type HostelPublicApiResource =
  | 'getHostels'
  | 'getById'

export const hostelPublicApiResource: ApiResources<HostelPublicApiResource> = {
  getHostels:{method: 'get' , url:'/hostel'},
  getById: { method: 'get', url: '/hostel/:id' },
};

export const fetchHostelList = async (params: any) => {
  try {
    const response = await axios.get('/api/auth/hostel', { params });
    return response.data; 
  } catch (error) {
    console.error("API Error:", error);
    return { list: [] }; 
  }
};