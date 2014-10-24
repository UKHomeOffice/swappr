require 'spec_helper'

def login_user
  visit HOST
  fill_in('Username', :with => 'admin')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end