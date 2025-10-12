import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { of } from 'rxjs';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let spiedHttpClientPost: jest.SpyInstance;

  beforeEach(() => {
    jest.restoreAllMocks();
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(AuthService);
    spiedHttpClientPost = jest.spyOn(TestBed.inject(HttpClient), 'post');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call httpClient.post() and return an empty observable on register', () => {
    const mockedRegisterRequest = {
      email: "test@test.com",
      firstName: "Test",
      lastName: "TEST",
      password: "test!1234"
    };
    spiedHttpClientPost.mockReturnValue(of());

    service.register(mockedRegisterRequest).subscribe(_ =>
      expect(spiedHttpClientPost).toHaveBeenCalledWith(`${service['pathService']}/register`, mockedRegisterRequest))
  });

  it('should call httpClient.post() and return an observable that emits session information on login', () => {
    const mockedLoginRequest = {
      email: "yoga@studio.com",
      password: "test!1234"
    };
    const mockedSessionInformation = {
      token: "jwt",
      type: "type",
      id: 1,
      username: "yoga@studio.com",
      firstName: "Admin",
      lastName: "Admin",
      admin: true
    };
    spiedHttpClientPost.mockReturnValue(of(mockedSessionInformation));

    service.login(mockedLoginRequest).subscribe(value => {
      expect(spiedHttpClientPost).toHaveBeenCalledWith(`${service['pathService']}/login`, mockedLoginRequest);
      expect(value).toEqual(mockedSessionInformation);
    });
  });
});
