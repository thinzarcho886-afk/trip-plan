import { ApiResources } from '../index.js';

// define api resource name
type BookingApiResource =
  | 'getBookings'
  | 'register'
  | 'update'
  | 'getById'
  | 'getBookingPublic'
  | 'registerPublic'
  | 'getByCustomerId';


export const bookingApiResource: ApiResources<BookingApiResource> = {
  getBookings: { method: 'get', url: '/auth/booking' },
  register: { method: 'post', url: '/auth/booking' },
  update: { method: 'put', url: '/auth/booking' },
  getById: { method: 'get', url: '/auth/booking/:id' },
  getBookingPublic: { method: 'get', url: '/booking' },
  registerPublic: { method: 'post', url: '/booking' },
  getByCustomerId: { method: 'get', url: '/booking/:customerId' },

};
