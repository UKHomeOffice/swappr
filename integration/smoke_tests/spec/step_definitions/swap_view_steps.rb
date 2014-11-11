def assert_first_event_heading(heading)
  expect(page.all('.t-event-heading').first).to have_content(heading)
end

def assert_last_event_heading(heading)
  expect(page.all('.t-event-heading').last).to have_content(heading)
end

def assert_your_new_shift_details(who, shift, date)
  expect(page.all('.event__swap-accepted .event__rota-you > .event__rota-person')[0]).to have_content(who)
  expect(page.all('.event__swap-accepted .event__rota-you > .event__rota-shift')[0]).to have_content(shift)
  expect(page.all('.event__swap-accepted .event__rota-you > .event__rota-date')[0]).to have_content(Time.parse(date).strftime("%A, %d %B"))
end

def assert_their_new_shift_details(who, shift, date)
  expect(page.all('.event__swap-accepted .event__rota-them > .event__rota-person')[0]).to have_content(who)
  expect(page.all('.event__swap-accepted .event__rota-them > .event__rota-shift')[0]).to have_content(shift)
  expect(page.all('.event__swap-accepted .event__rota-them > .event__rota-date')[0]).to have_content(Time.parse(date).strftime("%A, %d %B"))
end