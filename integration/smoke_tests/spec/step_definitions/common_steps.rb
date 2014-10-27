require 'spec_helper'

def login_user_bill
  visit HOST
  fill_in('Username', :with => 'bill')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def login_user_ben
  visit HOST
  fill_in('Username', :with => 'ben')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def logout
  find('.t-user-settings').click
  click_link('Logout')
end

def add_shift(day_index, shift_type)
  days = page.all('.t-day')
  days[day_index].find('.t-add-shift').click
  select(shift_type, :from => 'Shift')
  click_on('Submit')
end

def offer_shift(day_index, shift_type_from, shift_type_to)
  add_shift(day_index, shift_type_from)
  days = page.all('.t-day')
  days[day_index].find('.t-swap-shift').click
  select(shift_type_to, :from => 'toShiftType')
  click_on('Create')
end

def find_day_row_by_date(date)
  find(:xpath, "//*[@data-t-date='" + date + "']")
end