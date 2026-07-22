import { Status } from '../constants/Status.js';
import { CommonDto } from '../interfaces/CommonDto.js';

export interface BookingListParams {
  [key: string]: any;
  packageId: number | null;
  customerId: number | null;
  paymentMethodId: number | null;
  travelersQty: number | null;
  status: Status | string;
  note: string;
  packageName: string;
  customerName: string;
  paymentMethodName: string;
  totalAmount: number | null;
  departureDate: string;


}

export const BookingListParamsModel = (): BookingListParams => ({
  packageId: null,
  customerId: null,
  paymentMethodId: null,
  travelersQty: null,
  status: '',
  note: '',
  packageName: '',
  customerName: '',
  paymentMethodName: '',
  totalAmount: null,
  departureDate: '',

});

export interface Booking extends CommonDto {
  packageId: number | null;
  customerId: number | null;
  customerName: string;
  packageName: string;
  paymentMethodName: string;
  paymentMethodId: number | null;
  paymentReceiveImageUrl: string;
  travelersQty: number | null;
  status: Status | string;
  note: string;
  totalAmount: number | null;
  departureDate: string;
  busTypeName: string;
  busName: string;
  destinationName: string;
  durationName: string;
  hotelName: string;
  hotelFee: number | null;
  serviceFee: number | null;
  transportFee: number | null;
  budgetAmount: number | null;

}

export const BookingModel = (): Booking => ({
  packageId: null,
  packageName: '',
  customerName: '',
  paymentMethodName: '',
  customerId: null,
  paymentReceiveImageUrl: '',
  paymentMethodId: null,
  travelersQty: null,
  status: '',
  note: '',
  totalAmount: null,
  departureDate: '',
  busTypeName: '',
  busName: '',
  destinationName: '',
  durationName: '',
  hotelName: '',
  hotelFee: null,
  serviceFee: null,
  transportFee: null,
  budgetAmount: null,


});
