/// <reference types="cypress" />

describe('Register spec', () => {
  it('Register successful', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {} );

    cy.get('input[formControlName=firstName]').type("Test");
    cy.get('input[formControlName=lastName]').type("TEST");
    cy.get('input[formControlName=email]').type("test@test.com");
    cy.get('input[formControlName=password]').type(`${"test!4321"}{enter}{enter}`);

    cy.url().should('include', '/login');
  });
});