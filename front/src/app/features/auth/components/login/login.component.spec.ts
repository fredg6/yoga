import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;

  const mockedSessionInformation = {
    token: "jwt",
    type: "type",
    id: 1,
    username: "yoga@studio.com",
    firstName: "Admin",
    lastName: "Admin",
    admin: true
  };
  beforeEach(async () => {
    jest.restoreAllMocks();
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should correctly login user on submit', () => {
    const spiedAuthServiceLogin = jest.spyOn(authService, 'login').mockReturnValue(of(mockedSessionInformation));
    const spiedSessionServiceLogIn = jest.spyOn(sessionService, 'logIn');
    const router = TestBed.inject(Router);
    const spiedNavigate = jest.spyOn(router, 'navigate');
    
    component.form.setValue({ email: "yoga@studio.com", password: "test!1234" });
    component.submit();
    
    expect(spiedAuthServiceLogin).toHaveBeenCalled();
    expect(spiedSessionServiceLogIn).toHaveBeenCalledWith(mockedSessionInformation);
    expect(spiedNavigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should display error message', () => {
    const spiedAuthServiceLogin = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => {}));
    
    component.submit();
    
    expect(spiedAuthServiceLogin).toHaveBeenCalled();
    const errorPElement = fixture.nativeElement.querySelector("p.error");
    expect(errorPElement).toBeDefined();
  });
});
